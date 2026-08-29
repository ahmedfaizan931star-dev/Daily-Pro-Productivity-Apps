package com.dailyapps.kitebrief.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.kitebrief.data.local.AppDatabase
import com.dailyapps.kitebrief.data.model.Brief
import com.dailyapps.kitebrief.data.model.Commitment
import com.dailyapps.kitebrief.data.repository.KiteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class KiteViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = KiteRepository(AppDatabase.get(app))
    val today: String = LocalDate.now().toString()

    val brief: StateFlow<Brief?> = repo.observeBrief(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val commitments: StateFlow<List<Commitment>> = repo.observeCommitments(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val recent: StateFlow<List<Brief>> = repo.observeRecent()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveIntention(text: String, energy: Int) {
        viewModelScope.launch {
            val current = brief.value ?: Brief(dateKey = today)
            repo.saveBrief(current.copy(intention = text, energy = energy))
        }
    }

    fun addCommitment(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repo.addCommitment(Commitment(dateKey = today, title = title.trim()))
        }
    }

    fun toggle(item: Commitment) {
        viewModelScope.launch { repo.updateCommitment(item.copy(done = !item.done)) }
    }

    fun remove(item: Commitment) {
        viewModelScope.launch { repo.deleteCommitment(item.id) }
    }

    fun shutdown(note: String, score: Int) {
        viewModelScope.launch {
            val current = brief.value ?: Brief(dateKey = today)
            repo.saveBrief(current.copy(shutdownNote = note, landingScore = score, shutDown = true))
        }
    }
}
