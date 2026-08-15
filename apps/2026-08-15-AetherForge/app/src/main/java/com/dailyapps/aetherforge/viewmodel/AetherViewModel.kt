package com.dailyapps.aetherforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.aetherforge.data.model.DailyStats
import com.dailyapps.aetherforge.data.model.EnergyLevel
import com.dailyapps.aetherforge.data.model.FocusSessionEntity
import com.dailyapps.aetherforge.data.model.Priority
import com.dailyapps.aetherforge.data.model.TaskEntity
import com.dailyapps.aetherforge.data.repository.AetherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TimerState(
    val isRunning: Boolean = false,
    val remainingSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val selectedMinutes: Int = 25
)

class AetherViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AetherRepository(application)

    val tasks: StateFlow<List<TaskEntity>> = repo.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTasks: StateFlow<List<TaskEntity>> = repo.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<FocusSessionEntity>> = repo.getRecentSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _stats = MutableStateFlow(DailyStats())
    val stats: StateFlow<DailyStats> = _stats.asStateFlow()

    private val _timer = MutableStateFlow(TimerState())
    val timer: StateFlow<TimerState> = _timer.asStateFlow()

    private var timerJob: Job? = null

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _stats.value = repo.getDailyStats()
        }
    }

    fun addTask(title: String, description: String = "", priority: Priority = Priority.MEDIUM, energy: EnergyLevel = EnergyLevel.MEDIUM) {
        viewModelScope.launch {
            repo.addTask(title, description, priority, energy)
            refreshStats()
        }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repo.toggleComplete(task)
            refreshStats()
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repo.deleteTask(id)
            refreshStats()
        }
    }

    fun setTimerMinutes(minutes: Int) {
        if (!_timer.value.isRunning) {
            _timer.value = TimerState(remainingSeconds = minutes * 60, totalSeconds = minutes * 60, selectedMinutes = minutes)
        }
    }

    fun startTimer() {
        if (_timer.value.isRunning) return
        _timer.value = _timer.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                delay(1000)
                _timer.value = _timer.value.copy(remainingSeconds = _timer.value.remainingSeconds - 1)
            }
            if (_timer.value.remainingSeconds <= 0) {
                val mins = _timer.value.selectedMinutes
                repo.logFocusSession(null, mins)
                _timer.value = TimerState(selectedMinutes = mins, remainingSeconds = mins * 60, totalSeconds = mins * 60)
                refreshStats()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timer.value = _timer.value.copy(isRunning = false)
    }

    fun resetTimer() {
        timerJob?.cancel()
        val mins = _timer.value.selectedMinutes
        _timer.value = TimerState(selectedMinutes = mins, remainingSeconds = mins * 60, totalSeconds = mins * 60)
    }
}
