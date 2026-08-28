package com.dailyapps.stillpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.stillpoint.data.model.FocusSession
import com.dailyapps.stillpoint.data.model.Intention
import com.dailyapps.stillpoint.data.model.QuietBlock
import com.dailyapps.stillpoint.data.repository.StillRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StillUiState(
    val blocks: List<QuietBlock> = emptyList(),
    val intentions: List<Intention> = emptyList(),
    val sessions: List<FocusSession> = emptyList(),
    val timerRunning: Boolean = false,
    val remainingSec: Int = 25 * 60,
    val selectedMinutes: Int = 25
)

class StillViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StillRepository(app)

    private val timerRunning = MutableStateFlow(false)
    private val remainingSec = MutableStateFlow(25 * 60)
    private val selectedMinutes = MutableStateFlow(25)
    private var tickJob: Job? = null

    val state: StateFlow<StillUiState> = combine(
        repo.blocks,
        repo.intentions,
        repo.sessions,
        timerRunning,
        remainingSec
    ) { blocks, intentions, sessions, running, remain ->
        StillUiState(
            blocks = blocks,
            intentions = intentions,
            sessions = sessions,
            timerRunning = running,
            remainingSec = remain,
            selectedMinutes = selectedMinutes.value
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StillUiState())

    fun addBlock(title: String, startHour: Int, durationMin: Int) {
        viewModelScope.launch { repo.addBlock(title.ifBlank { "Quiet block" }, startHour, durationMin) }
    }

    fun toggleBlock(block: QuietBlock) {
        viewModelScope.launch { repo.toggleBlock(block) }
    }

    fun deleteBlock(block: QuietBlock) {
        viewModelScope.launch { repo.deleteBlock(block) }
    }

    fun addIntention(text: String, energy: Int) {
        if (text.isBlank()) return
        viewModelScope.launch { repo.addIntention(text.trim(), energy) }
    }

    fun toggleIntention(item: Intention) {
        viewModelScope.launch { repo.toggleIntention(item) }
    }

    fun deleteIntention(item: Intention) {
        viewModelScope.launch { repo.deleteIntention(item) }
    }

    fun setDuration(minutes: Int) {
        if (timerRunning.value) return
        selectedMinutes.value = minutes
        remainingSec.value = minutes * 60
    }

    fun toggleTimer() {
        if (timerRunning.value) {
            timerRunning.value = false
            tickJob?.cancel()
        } else {
            if (remainingSec.value <= 0) remainingSec.value = selectedMinutes.value * 60
            timerRunning.value = true
            tickJob = viewModelScope.launch {
                while (remainingSec.value > 0 && timerRunning.value) {
                    delay(1000)
                    remainingSec.value = remainingSec.value - 1
                }
                if (remainingSec.value <= 0) {
                    timerRunning.value = false
                    repo.logSession(selectedMinutes.value, "Deep quiet session")
                    remainingSec.value = selectedMinutes.value * 60
                }
            }
        }
    }

    fun resetTimer() {
        tickJob?.cancel()
        timerRunning.value = false
        remainingSec.value = selectedMinutes.value * 60
    }
}
