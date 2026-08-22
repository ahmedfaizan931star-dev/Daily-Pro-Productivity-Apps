package com.dailyapps.solsticeflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.solsticeflow.data.local.AppDatabase
import com.dailyapps.solsticeflow.data.model.DailyReview
import com.dailyapps.solsticeflow.data.model.EnergyLog
import com.dailyapps.solsticeflow.data.model.FocusSession
import com.dailyapps.solsticeflow.data.model.Habit
import com.dailyapps.solsticeflow.data.repository.SolsticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val energyLogs: List<EnergyLog> = emptyList(),
    val habits: List<Habit> = emptyList(),
    val todayFocusMinutes: Int = 0,
    val recentSessions: List<FocusSession> = emptyList(),
    val todayReview: DailyReview? = null,
    val timerRunning: Boolean = false,
    val timerSecondsLeft: Int = 25 * 60,
    val selectedEnergy: Int = 3
)

class SolsticeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SolsticeRepository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val energyLogs = repo.getTodayEnergyLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits = repo.getHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayFocus = repo.getTodayFocusMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            energyLogs.collect { logs ->
                _uiState.value = _uiState.value.copy(energyLogs = logs)
            }
        }
        viewModelScope.launch {
            habits.collect { h ->
                _uiState.value = _uiState.value.copy(habits = h)
            }
        }
        viewModelScope.launch {
            todayFocus.collect { m ->
                _uiState.value = _uiState.value.copy(todayFocusMinutes = m)
            }
        }
        viewModelScope.launch {
            val review = repo.getTodayReview()
            _uiState.value = _uiState.value.copy(todayReview = review)
        }
    }

    fun logEnergy(level: Int, note: String = "") {
        viewModelScope.launch {
            repo.addEnergyLog(level, note)
            _uiState.value = _uiState.value.copy(selectedEnergy = level)
        }
    }

    fun addHabit(title: String, icon: String = "☀️") {
        viewModelScope.launch { repo.addHabit(title, icon) }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch { repo.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repo.deleteHabit(id) }
    }

    fun startTimer(minutes: Int = 25) {
        _uiState.value = _uiState.value.copy(
            timerRunning = true,
            timerSecondsLeft = minutes * 60
        )
    }

    fun tickTimer() {
        val current = _uiState.value.timerSecondsLeft
        if (current > 0) {
            _uiState.value = _uiState.value.copy(timerSecondsLeft = current - 1)
        } else {
            completeTimer()
        }
    }

    fun pauseTimer() {
        _uiState.value = _uiState.value.copy(timerRunning = false)
    }

    fun completeTimer() {
        val minutes = 25 // default completed
        viewModelScope.launch {
            repo.saveFocusSession(minutes, _uiState.value.selectedEnergy)
            _uiState.value = _uiState.value.copy(
                timerRunning = false,
                timerSecondsLeft = 25 * 60
            )
        }
    }

    fun resetTimer() {
        _uiState.value = _uiState.value.copy(
            timerRunning = false,
            timerSecondsLeft = 25 * 60
        )
    }

    fun saveReview(wins: String, challenges: String) {
        viewModelScope.launch {
            val avg = _uiState.value.energyLogs.map { it.level }.average().toFloat().takeIf { !it.isNaN() } ?: 0f
            repo.saveReview(wins, challenges, avg, _uiState.value.todayFocusMinutes)
            _uiState.value = _uiState.value.copy(
                todayReview = DailyReview(
                    date = java.time.LocalDate.now().toString(),
                    wins = wins,
                    challenges = challenges,
                    energyAverage = avg,
                    focusMinutes = _uiState.value.todayFocusMinutes
                )
            )
        }
    }
}
