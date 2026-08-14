package com.dailyapps.apexflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.apexflow.data.model.DailyStats
import com.dailyapps.apexflow.data.model.FocusSession
import com.dailyapps.apexflow.data.model.Priority
import com.dailyapps.apexflow.data.model.Task
import com.dailyapps.apexflow.data.repository.ApexRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ApexViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ApexRepository(application)

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTasks: StateFlow<List<Task>> = repository.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<FocusSession>> = repository.getAllSessions()
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

    fun addTask(title: String, priority: Priority) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTask(title.trim(), priority)
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
            refreshStats()
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
        viewModelScope.launch {
            while (_isTimerRunning.value && _timerSeconds.value > 0) {
                kotlinx.coroutines.delay(1000)
                if (_isTimerRunning.value) {
                    _timerSeconds.value = _timerSeconds.value - 1
                }
            }
            if (_timerSeconds.value == 0 && _isTimerRunning.value) {
                _isTimerRunning.value = false
                repository.addFocusSession(_selectedMinutes.value)
                refreshStats()
                _timerSeconds.value = _selectedMinutes.value * 60
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
    }

    fun resetTimer() {
        _isTimerRunning.value = false
        _timerSeconds.value = _selectedMinutes.value * 60
    }

    fun completeSessionManually() {
        viewModelScope.launch {
            repository.addFocusSession(_selectedMinutes.value)
            refreshStats()
            resetTimer()
        }
    }
}
