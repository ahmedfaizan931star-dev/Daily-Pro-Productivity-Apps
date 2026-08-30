package com.dailyapps.ridgenote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.ridgenote.data.local.AppDatabase
import com.dailyapps.ridgenote.data.model.Decision
import com.dailyapps.ridgenote.data.repository.RidgeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RidgeUiState(
    val decisions: List<Decision> = emptyList(),
    val pending: Int = 0,
    val reviewed: Int = 0,
    val hitRate: Int = 0,
    val avgConfidence: Int = 0,
    val calibrationGap: Int = 0
)

class RidgeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RidgeRepository(AppDatabase.get(app).decisionDao())

    val uiState: StateFlow<RidgeUiState> = repo.observe().map { list ->
        val reviewed = list.filter { it.outcome != "pending" }
        val hits = reviewed.count { it.outcome == "hit" }
        val rate = if (reviewed.isEmpty()) 0 else (hits * 100 / reviewed.size)
        val avg = if (reviewed.isEmpty()) 0 else reviewed.map { it.confidence }.average().toInt()
        RidgeUiState(
            decisions = list,
            pending = list.count { it.outcome == "pending" },
            reviewed = reviewed.size,
            hitRate = rate,
            avgConfidence = avg,
            calibrationGap = avg - rate
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RidgeUiState())

    fun add(title: String, context: String, choice: String, confidence: Int, domain: String) {
        viewModelScope.launch {
            repo.add(
                Decision(
                    title = title.trim(),
                    context = context.trim(),
                    choice = choice.trim(),
                    confidence = confidence.coerceIn(0, 100),
                    domain = domain
                )
            )
        }
    }

    fun setOutcome(item: Decision, outcome: String, note: String = item.reviewNote) {
        viewModelScope.launch { repo.update(item.copy(outcome = outcome, reviewNote = note)) }
    }

    fun delete(item: Decision) {
        viewModelScope.launch { repo.delete(item) }
    }
}
