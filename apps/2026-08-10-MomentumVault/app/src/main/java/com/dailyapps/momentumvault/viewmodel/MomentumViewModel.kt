package com.dailyapps.momentumvault.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.momentumvault.data.model.EnergyLog
import com.dailyapps.momentumvault.data.model.FocusSession
import com.dailyapps.momentumvault.data.model.Habit
import com.dailyapps.momentumvault.data.model.HabitLog
import com.dailyapps.momentumvault.data.repository.MomentumRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MomentumUiState(
    val habits: List<Habit> = emptyList(),
    val todayLogs: List<HabitLog> = emptyList(),
    val todaySessions: List<FocusSession> = emptyList(),
    val todayEnergy: EnergyLog? = null,
    val momentumScore: Int = 0,
    val focusMinutesToday: Int = 0,
    val completedHabitsToday: Int = 0
)

class MomentumViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MomentumRepository(application)

    private val _uiState = MutableStateFlow(MomentumUiState())
    val uiState: StateFlow<MomentumUiState> = _uiState.asStateFlow()

    // Timer state
    private val _timerSeconds = MutableStateFlow(25 * 60)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _selectedMinutes = MutableStateFlow(25)
    val selectedMinutes: StateFlow<Int> = _selectedMinutes.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                repo.getHabits(),
                repo.getLogsForDate(),
                repo.getTodaySessions(),
                repo.getTodayEnergy()
            ) { habits, logs, sessions, energy ->
                val completed = logs.count { it.completed }
                val focusMin = sessions.sumOf { it.durationMinutes }
                val score = repo.calculateMomentum(
                    habits = habits,
                    completedToday = completed,
                    focusMinutesToday = focusMin,
                    hasReflection = energy != null
                )
                MomentumUiState(
                    habits = habits,
                    todayLogs = logs,
                    todaySessions = sessions,
                    todayEnergy = energy,
                    momentumScore = score,
                    focusMinutesToday = focusMin,
                    completedHabitsToday = completed
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repo.addHabit(title.trim()) }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch { repo.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repo.deleteHabit(id) }
    }

    fun isHabitDoneToday(habit: Habit): Boolean {
        return habit.lastCompletedDate == repo.today() ||
                _uiState.value.todayLogs.any { it.habitId == habit.id && it.completed }
    }

    // Timer
    fun selectDuration(minutes: Int) {
        if (_isRunning.value) return
        _selectedMinutes.value = minutes
        _timerSeconds.value = minutes * 60
    }

    fun startPauseTimer() {
        if (_isRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        if (_timerSeconds.value <= 0) {
            _timerSeconds.value = _selectedMinutes.value * 60
        }
        _isRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0 && _isRunning.value) {
                delay(1000)
                _timerSeconds.value = _timerSeconds.value - 1
            }
            if (_timerSeconds.value <= 0) {
                _isRunning.value = false
                completeSession()
            }
        }
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerSeconds.value = _selectedMinutes.value * 60
    }

    private fun completeSession() {
        viewModelScope.launch {
            repo.addFocusSession(_selectedMinutes.value)
            _timerSeconds.value = _selectedMinutes.value * 60
        }
    }

    fun saveEnergy(energy: Int, mood: Int, journal: String) {
        viewModelScope.launch {
            repo.saveEnergyLog(energy, mood, journal)
        }
    }

    fun formatTime(totalSeconds: Int): String {
        val m = totalSeconds / 60
        val s = totalSeconds % 60
        return "%02d:%02d".format(m, s)
    }
}
