package com.dailyapps.luminafocus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.luminafocus.data.model.EnergyLog
import com.dailyapps.luminafocus.data.model.FocusSession
import com.dailyapps.luminafocus.data.model.Habit
import com.dailyapps.luminafocus.data.repository.LuminaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TimerState(
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val mode: String = "pomodoro"
)

data class UiState(
    val todayMinutes: Int = 0,
    val todaySessions: List<FocusSession> = emptyList(),
    val habits: List<Habit> = emptyList(),
    val energyLogs: List<EnergyLog> = emptyList(),
    val timer: TimerState = TimerState()
)

class LuminaViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = LuminaRepository(application)

    private val _timer = MutableStateFlow(TimerState())
    val timer: StateFlow<TimerState> = _timer.asStateFlow()

    private var timerJob: Job? = null

    val uiState: StateFlow<UiState> = combine(
        repo.getTodayFocusMinutes(),
        repo.getTodaySessions(),
        repo.getHabits(),
        repo.getEnergyLogs(),
        _timer
    ) { minutes, sessions, habits, logs, timer ->
        UiState(
            todayMinutes = minutes,
            todaySessions = sessions,
            habits = habits,
            energyLogs = logs,
            timer = timer
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiState()
    )

    fun setTimerPreset(minutes: Int, mode: String = "pomodoro") {
        timerJob?.cancel()
        _timer.value = TimerState(
            totalSeconds = minutes * 60,
            remainingSeconds = minutes * 60,
            isRunning = false,
            mode = mode
        )
    }

    fun startOrPauseTimer() {
        val current = _timer.value
        if (current.isRunning) {
            timerJob?.cancel()
            _timer.value = current.copy(isRunning = false)
        } else {
            _timer.value = current.copy(isRunning = true)
            timerJob = viewModelScope.launch {
                while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                    delay(1000)
                    val t = _timer.value
                    if (t.isRunning) {
                        val next = t.remainingSeconds - 1
                        _timer.value = t.copy(remainingSeconds = next)
                        if (next <= 0) {
                            completeSession()
                        }
                    }
                }
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val total = _timer.value.totalSeconds
        _timer.value = _timer.value.copy(
            remainingSeconds = total,
            isRunning = false
        )
    }

    private fun completeSession() {
        val minutes = _timer.value.totalSeconds / 60
        val mode = _timer.value.mode
        viewModelScope.launch {
            repo.addSession(minutes, mode)
        }
        _timer.value = _timer.value.copy(isRunning = false, remainingSeconds = 0)
    }

    fun addHabit(title: String, icon: String = "⭐") {
        viewModelScope.launch { repo.addHabit(title, icon) }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch { repo.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repo.deleteHabit(id) }
    }

    fun logEnergy(energy: Int, mood: Int, note: String = "") {
        viewModelScope.launch { repo.logEnergy(energy, mood, note) }
    }
}
