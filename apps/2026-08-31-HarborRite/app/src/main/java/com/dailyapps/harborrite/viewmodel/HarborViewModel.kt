package com.dailyapps.harborrite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.harborrite.data.model.Berth
import com.dailyapps.harborrite.data.model.Rite
import com.dailyapps.harborrite.data.model.Voyage
import com.dailyapps.harborrite.data.repository.HarborRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HarborUiState(
    val berths: List<Berth> = emptyList(),
    val voyages: List<Voyage> = emptyList(),
    val rites: List<Rite> = emptyList()
) {
    val weekStart: Long
        get() {
            val now = System.currentTimeMillis()
            return now - 7L * 24 * 60 * 60 * 1000
        }

    val weeklyMinutes: Int
        get() = voyages.filter { it.createdAt >= weekStart }.sumOf { it.minutes }

    fun loadFor(berthId: Long): Int =
        voyages.filter { it.berthId == berthId }.sumOf { it.minutes * it.tide }

    val busiest: Berth?
        get() = berths.maxByOrNull { loadFor(it.id) }
}

class HarborViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HarborRepository(app)

    val state: StateFlow<HarborUiState> = combine(
        repo.berths, repo.voyages, repo.rites
    ) { b, v, r -> HarborUiState(b, v, r) }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HarborUiState()
    )

    fun addBerth(name: String, intent: String) = viewModelScope.launch {
        if (name.isNotBlank()) repo.addBerth(name, intent)
    }

    fun deleteBerth(item: Berth) = viewModelScope.launch { repo.deleteBerth(item) }

    fun addVoyage(berthId: Long, minutes: Int, tide: Int, note: String) =
        viewModelScope.launch { repo.addVoyage(berthId, minutes, tide, note) }

    fun deleteVoyage(item: Voyage) = viewModelScope.launch { repo.deleteVoyage(item) }

    fun addRite(berthId: Long, reflection: String) = viewModelScope.launch {
        if (reflection.isNotBlank()) repo.addRite(berthId, reflection)
    }

    fun deleteRite(item: Rite) = viewModelScope.launch { repo.deleteRite(item) }
}
