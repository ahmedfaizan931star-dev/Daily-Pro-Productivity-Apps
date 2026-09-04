package com.dailyapps.sablequorum.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.sablequorum.SableQuorumApp
import com.dailyapps.sablequorum.data.Decision
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuorumUiState(
    val decisions: List<Decision> = emptyList(),
    val filter: String = "OPEN",
    val draftTitle: String = "",
    val draftContext: String = "",
    val draftOptions: String = "",
    val draftCriteria: String = "",
    val draftChosen: String = "",
    val draftExpected: String = "",
    val draftDomain: String = "Work",
    val draftUrgency: Int = 2,
    val draftConfidence: Int = 60,
    val selected: Decision? = null,
    val reviewNotes: String = ""
) {
    val visible: List<Decision>
        get() = when (filter) {
            "OPEN" -> decisions.filter { it.status == "OPEN" }
            "REVIEWED" -> decisions.filter { it.status == "REVIEWED" }
            else -> decisions
        }
    val openCount get() = decisions.count { it.status == "OPEN" }
    val reviewedCount get() = decisions.count { it.status == "REVIEWED" }
    val avgConfidence: Int
        get() = if (decisions.isEmpty()) 0 else decisions.map { it.confidence }.average().toInt()
}

class QuorumViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as SableQuorumApp).repository
    private val filter = MutableStateFlow("OPEN")
    private val draftTitle = MutableStateFlow("")
    private val draftContext = MutableStateFlow("")
    private val draftOptions = MutableStateFlow("")
    private val draftCriteria = MutableStateFlow("")
    private val draftChosen = MutableStateFlow("")
    private val draftExpected = MutableStateFlow("")
    private val draftDomain = MutableStateFlow("Work")
    private val draftUrgency = MutableStateFlow(2)
    private val draftConfidence = MutableStateFlow(60)
    private val selected = MutableStateFlow<Decision?>(null)
    private val reviewNotes = MutableStateFlow("")

    val state: StateFlow<QuorumUiState> = combine(
        repo.observe(),
        filter,
        draftTitle,
        draftContext,
        draftOptions,
        draftCriteria,
        draftChosen,
        draftExpected,
        draftDomain,
        draftUrgency,
        draftConfidence,
        selected,
        reviewNotes
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        QuorumUiState(
            decisions = values[0] as List<Decision>,
            filter = values[1] as String,
            draftTitle = values[2] as String,
            draftContext = values[3] as String,
            draftOptions = values[4] as String,
            draftCriteria = values[5] as String,
            draftChosen = values[6] as String,
            draftExpected = values[7] as String,
            draftDomain = values[8] as String,
            draftUrgency = values[9] as Int,
            draftConfidence = values[10] as Int,
            selected = values[11] as Decision?,
            reviewNotes = values[12] as String
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QuorumUiState())

    fun setFilter(value: String) { filter.value = value }
    fun setTitle(v: String) { draftTitle.value = v }
    fun setContext(v: String) { draftContext.value = v }
    fun setOptions(v: String) { draftOptions.value = v }
    fun setCriteria(v: String) { draftCriteria.value = v }
    fun setChosen(v: String) { draftChosen.value = v }
    fun setExpected(v: String) { draftExpected.value = v }
    fun setDomain(v: String) { draftDomain.value = v }
    fun setUrgency(v: Int) { draftUrgency.value = v }
    fun setConfidence(v: Int) { draftConfidence.value = v }
    fun select(d: Decision?) {
        selected.value = d
        reviewNotes.value = d?.reviewNotes.orEmpty()
    }
    fun setReviewNotes(v: String) { reviewNotes.value = v }

    fun saveDraft() {
        val s = state.value
        if (s.draftTitle.isBlank()) return
        viewModelScope.launch {
            repo.save(
                Decision(
                    title = s.draftTitle.trim(),
                    context = s.draftContext.trim(),
                    options = s.draftOptions.trim(),
                    criteria = s.draftCriteria.trim(),
                    chosen = s.draftChosen.trim(),
                    expectedOutcome = s.draftExpected.trim(),
                    domain = s.draftDomain,
                    urgency = s.draftUrgency,
                    confidence = s.draftConfidence,
                    status = "OPEN",
                    reviewNotes = "",
                    createdAt = System.currentTimeMillis(),
                    reviewedAt = null
                )
            )
            draftTitle.value = ""
            draftContext.value = ""
            draftOptions.value = ""
            draftCriteria.value = ""
            draftChosen.value = ""
            draftExpected.value = ""
            draftUrgency.value = 2
            draftConfidence.value = 60
        }
    }

    fun closeReview() {
        val current = selected.value ?: return
        viewModelScope.launch {
            repo.update(
                current.copy(
                    status = "REVIEWED",
                    reviewNotes = reviewNotes.value.trim(),
                    reviewedAt = System.currentTimeMillis()
                )
            )
            selected.value = null
        }
    }

    fun delete(decision: Decision) {
        viewModelScope.launch { repo.delete(decision) }
        if (selected.value?.id == decision.id) selected.value = null
    }

    fun seedIfEmpty() {
        viewModelScope.launch {
            // no-op placeholder; board starts empty on purpose
        }
    }
}
