package com.dailyapps.amberkiln.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.amberkiln.data.model.Cooldown
import com.dailyapps.amberkiln.data.model.FireSession
import com.dailyapps.amberkiln.data.model.Kiln
import com.dailyapps.amberkiln.data.repository.AmberRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AmberUiState(
    val kilns: List<Kiln> = emptyList(),
    val sessions: List<FireSession> = emptyList(),
    val cooldowns: List<Cooldown> = emptyList()
) {
    val weekStart: Long
        get() {
            val now = System.currentTimeMillis()
            return now - 7L * 24 * 60 * 60 * 1000
        }

    val weeklyMinutes: Int
        get() = sessions.filter { it.createdAt >= weekStart }.sumOf { it.minutes }

    fun heatFor(kilnId: Long): Int =
        sessions.filter { it.kilnId == kilnId }.sumOf { it.minutes * it.intensity }

    val hottest: Kiln?
        get() = kilns.maxByOrNull { heatFor(it.id) }
}

class AmberViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AmberRepository(app)

    val state: StateFlow<AmberUiState> = combine(
        repo.kilns, repo.sessions, repo.cooldowns
    ) { k, s, c -> AmberUiState(k, s, c) }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AmberUiState()
    )

    fun addKiln(name: String, intent: String) = viewModelScope.launch {
        if (name.isNotBlank()) repo.addKiln(name, intent)
    }

    fun deleteKiln(item: Kiln) = viewModelScope.launch { repo.deleteKiln(item) }

    fun addSession(kilnId: Long, minutes: Int, intensity: Int, note: String) =
        viewModelScope.launch { repo.addSession(kilnId, minutes, intensity, note) }

    fun deleteSession(item: FireSession) = viewModelScope.launch { repo.deleteSession(item) }

    fun addCooldown(kilnId: Long, reflection: String) = viewModelScope.launch {
        if (reflection.isNotBlank()) repo.addCooldown(kilnId, reflection)
    }

    fun deleteCooldown(item: Cooldown) = viewModelScope.launch { repo.deleteCooldown(item) }
}
