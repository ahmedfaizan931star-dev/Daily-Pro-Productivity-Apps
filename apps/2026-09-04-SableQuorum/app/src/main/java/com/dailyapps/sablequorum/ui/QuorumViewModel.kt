package com.dailyapps.sablequorum.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.sablequorum.SableQuorumApp
import com.dailyapps.sablequorum.data.Decision
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val _state = MutableStateFlow(QuorumUiState())
    val state: StateFlow<QuorumUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observe().collect { list ->
                _state.update { it.copy(decisions = list) }
            }
        }
    }

    fun setFilter(value: String) { _state.update { it.copy(filter = value) } }
    fun setTitle(v: String) { _state.update { it.copy(draftTitle = v) } }
    fun setContext(v: String) { _state.update { it.copy(draftContext = v) } }
    fun setOptions(v: String) { _state.update { it.copy(draftOptions = v) } }
    fun setCriteria(v: String) { _state.update { it.copy(draftCriteria = v) } }
    fun setChosen(v: String) { _state.update { it.copy(draftChosen = v) } }
    fun setExpected(v: String) { _state.update { it.copy(draftExpected = v) } }
    fun setDomain(v: String) { _state.update { it.copy(draftDomain = v) } }
    fun setUrgency(v: Int) { _state.update { it.copy(draftUrgency = v) } }
    fun setConfidence(v: Int) { _state.update { it.copy(draftConfidence = v) } }
    fun select(d: Decision?) {
        _state.update { it.copy(selected = d, reviewNotes = d?.reviewNotes.orEmpty()) }
    }
    fun setReviewNotes(v: String) { _state.update { it.copy(reviewNotes = v) } }

    fun saveDraft() {
        val s = _state.value
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
            _state.update {
                it.copy(
                    draftTitle = "",
                    draftContext = "",
                    draftOptions = "",
                    draftCriteria = "",
                    draftChosen = "",
                    draftExpected = "",
                    draftUrgency = 2,
                    draftConfidence = 60
                )
            }
        }
    }

    fun closeReview() {
        val current = _state.value.selected ?: return
        val notes = _state.value.reviewNotes
        viewModelScope.launch {
            repo.update(
                current.copy(
                    status = "REVIEWED",
                    reviewNotes = notes.trim(),
                    reviewedAt = System.currentTimeMillis()
                )
            )
            _state.update { it.copy(selected = null, reviewNotes = "") }
        }
    }

    fun delete(decision: Decision) {
        viewModelScope.launch { repo.delete(decision) }
        if (_state.value.selected?.id == decision.id) {
            _state.update { it.copy(selected = null) }
        }
    }
}
