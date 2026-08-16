package com.dailyapps.orbitmind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.orbitmind.data.model.DailyStats
import com.dailyapps.orbitmind.data.model.FocusSession
import com.dailyapps.orbitmind.data.model.Habit
import com.dailyapps.orbitmind.data.model.Task
import com.dailyapps.orbitmind.data.repository.OrbitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrbitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrbitRepository(application)

    val activeTasks: StateFlow<List<Task>> = repository.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<Habit>> = repository.getHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<FocusSession>> = repository.getRecentSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _stats = MutableStateFlow(DailyStats())
    val stats: StateFlow<DailyStats> = _stats.asStateFlow()

    private val _timerSeconds = MutableStateFlow(25 * 60)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _selectedMinutes = MutableStateFlow(25)
    val selectedMinutes: StateFlow<Int> = _selectedMinutes.asStateFlow()

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _stats.value = repository.getDailyStats()
        }
    }

    fun addTask(title: String, description: String = "", orbitLevel: Int = 2, minutes: Int = 25) {
        viewModelScope.launch {
            repository.addTask(title, description, orbitLevel, minutes)
            refreshStats()
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.toggleTask(task)
            refreshStats()
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun addHabit(name: String, icon: String = "🌟") {
        viewModelScope.launch {
            repository.addHabit(name, icon)
        }
    }

    fun completeHabit(habit: Habit) {
        viewModelScope.launch {
            repository.completeHabit(habit)
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    fun setTimerMinutes(minutes: Int) {
        _selectedMinutes.value = minutes
        if (!_isTimerRunning.value) {
            _timerSeconds.value = minutes * 60
        }
    }

    fun startTimer() {
        if (_timerSeconds.value <= 0) {
            _timerSeconds.value = _selectedMinutes.value * 60
        }
        _isTimerRunning.value = true
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
    }

    fun resetTimer() {
        _isTimerRunning.value = false
        _timerSeconds.value = _selectedMinutes.value * 60
    }

    fun tick() {
        if (_isTimerRunning.value && _timerSeconds.value > 0) {
            _timerSeconds.value = _timerSeconds.value - 1
            if (_timerSeconds.value == 0) {
                _isTimerRunning.value = false
                viewModelScope.launch {
                    repository.logFocusSession(_selectedMinutes.value)
                    refreshStats()
                }
            }
        }
    }

    fun completeSessionEarly() {
        val elapsed = _selectedMinutes.value * 60 - _timerSeconds.value
        val mins = (elapsed / 60).coerceAtLeast(1)
        _isTimerRunning.value = false
        viewModelScope.launch {
            repository.logFocusSession(mins)
            refreshStats()
        }
        resetTimer()
    }
}
