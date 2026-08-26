package com.dailyapps.novafocus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.novafocus.data.model.Habit
import com.dailyapps.novafocus.data.model.Task
import com.dailyapps.novafocus.data.repository.NovaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class NovaUiState(
    val habits: List<Habit> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val focusMinutesToday: Int = 0,
    val isTimerRunning: Boolean = false,
    val timerSecondsLeft: Int = 25 * 60,
    val selectedDurationMinutes: Int = 25
)

class NovaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NovaRepository(application)

    private val _timerRunning = MutableStateFlow(false)
    private val _timerSeconds = MutableStateFlow(25 * 60)
    private val _selectedDuration = MutableStateFlow(25)
    private var timerJob: Job? = null

    val uiState: StateFlow<NovaUiState> = combine(
        repository.habits,
        repository.tasks,
        repository.focusMinutesToday(),
        _timerRunning,
        _timerSeconds,
        _selectedDuration
    ) { values ->
        val habits = values[0] as List<Habit>
        val tasks = values[1] as List<Task>
        val focusMin = values[2] as Int
        val running = values[3] as Boolean
        val seconds = values[4] as Int
        val duration = values[5] as Int
        NovaUiState(
            habits = habits,
            tasks = tasks,
            focusMinutesToday = focusMin,
            isTimerRunning = running,
            timerSecondsLeft = seconds,
            selectedDurationMinutes = duration
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NovaUiState()
    )

    fun addHabit(title: String, emoji: String = "\u2B50") {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addHabit(title.trim(), emoji) }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch { repository.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repository.deleteHabit(id) }
    }

    fun addTask(title: String, priority: Int = 1) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addTask(title.trim(), priority) }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch { repository.toggleTask(task) }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch { repository.deleteTask(id) }
    }

    fun setDuration(minutes: Int) {
        if (_timerRunning.value) return
        _selectedDuration.value = minutes
        _timerSeconds.value = minutes * 60
    }

    fun startOrPauseTimer() {
        if (_timerRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        if (_timerSeconds.value <= 0) {
            _timerSeconds.value = _selectedDuration.value * 60
        }
        _timerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0 && _timerRunning.value) {
                delay(1000)
                _timerSeconds.value = _timerSeconds.value - 1
            }
            if (_timerSeconds.value <= 0) {
                _timerRunning.value = false
                val minutes = _selectedDuration.value
                repository.recordFocusSession(minutes)
                _timerSeconds.value = minutes * 60
            }
        }
    }

    private fun pauseTimer() {
        _timerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerSeconds.value = _selectedDuration.value * 60
    }

    fun habitsCompletedToday(): Int {
        val today = LocalDate.now().toString()
        return uiState.value.habits.count { it.lastCompletedDate == today }
    }
}
