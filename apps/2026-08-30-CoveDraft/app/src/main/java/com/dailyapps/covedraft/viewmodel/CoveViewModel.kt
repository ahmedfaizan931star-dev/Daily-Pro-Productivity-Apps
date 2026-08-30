package com.dailyapps.covedraft.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.covedraft.data.model.Closeout
import com.dailyapps.covedraft.data.model.Decision
import com.dailyapps.covedraft.data.model.Draft
import com.dailyapps.covedraft.data.repository.CoveRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoveViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CoveRepository(app)

    val drafts: StateFlow<List<Draft>> = repo.drafts.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val decisions: StateFlow<List<Decision>> = repo.decisions.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val closeouts: StateFlow<List<Closeout>> = repo.closeouts.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    fun addDraft(title: String, note: String) = viewModelScope.launch { repo.addDraft(title, note) }
    fun toggleLaunch(draft: Draft) = viewModelScope.launch { repo.toggleLaunch(draft) }
    fun deleteDraft(id: Long) = viewModelScope.launch { repo.deleteDraft(id) }
    fun addDecision(request: String, verdict: String) =
        viewModelScope.launch { repo.addDecision(request, verdict) }
    fun deleteDecision(id: Long) = viewModelScope.launch { repo.deleteDecision(id) }
    fun addCloseout(win: String, leftover: String) =
        viewModelScope.launch { repo.addCloseout(win, leftover) }
}
