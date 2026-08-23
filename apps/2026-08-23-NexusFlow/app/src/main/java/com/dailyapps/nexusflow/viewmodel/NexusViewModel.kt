package com.dailyapps.nexusflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.nexusflow.data.local.AppDatabase
import com.dailyapps.nexusflow.data.model.Habit
import com.dailyapps.nexusflow.data.model.Task
import com.dailyapps.nexusflow.data.repository.NexusRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val habits: List<Habit> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val todayFocusMinutes: Int = 0,
    val isTimerRunning: Boolean = false,
    val remainingSeconds: Int = 25 * 60,
    val selectedDuration: Int = 25
)

class NexusViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = NexusRepository(AppDatabase.getInstance(application))

    private val _timerRunning = MutableStateFlow(false)
    private val _remaining = MutableStateFlow(25 * 60)
    private val _selectedDuration = MutableStateFlow(25)
    private val _todayFocus = MutableStateFlow(0)

    private var timerJob: Job? = null

    val uiState: StateFlow<UiState> = combine(
        repo.habits,
        repo.tasks,
        _todayFocus,
        combine(_timerRunning, _remaining, _selectedDuration) { running, remaining, duration ->
            Triple(running, remaining, duration)
        }
    ) { habits, tasks, focus, timerTriple ->
        UiState(
            habits = habits,
            tasks = tasks,
            todayFocusMinutes = focus,
            isTimerRunning = timerTriple.first,
            remainingSeconds = timerTriple.second,
            selectedDuration = timerTriple.third
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    init {
        refreshTodayFocus()
    }

    fun refreshTodayFocus() {
        viewModelScope.launch {
            _todayFocus.value = repo.todayFocusMinutes()
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

    fun addTask(title: String, priority: Int) {
        if (title.isBlank()) return
        viewModelScope.launch { repo.addTask(title.trim(), priority) }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch { repo.toggleTask(task) }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch { repo.deleteTask(id) }
    }

    fun setDuration(minutes: Int) {
        if (_timerRunning.value) return
        _selectedDuration.value = minutes
        _remaining.value = minutes * 60
    }

    fun startTimer() {
        if (_timerRunning.value) return
        _timerRunning.value = true
        timerJob = viewModelScope.launch {
            while (_remaining.value > 0 && _timerRunning.value) {
                delay(1000)
                _remaining.value = _remaining.value - 1
            }
            if (_remaining.value <= 0) {
                val mins = _selectedDuration.value
                repo.logFocusSession(mins)
                refreshTodayFocus()
                _timerRunning.value = false
                _remaining.value = mins * 60
            }
        }
    }

    fun pauseTimer() {
        _timerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _remaining.value = _selectedDuration.value * 60
    }
}
