package com.dailyapps.claritymatrix.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.claritymatrix.data.model.Quadrant
import com.dailyapps.claritymatrix.data.model.Task
import com.dailyapps.claritymatrix.data.repository.ClarityRepository
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
    val linkedTaskId: Long? = null,
    val linkedTaskTitle: String = ""
)

class ClarityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ClarityRepository(application)

    val activeTasks: StateFlow<List<Task>> = repository.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _timer = MutableStateFlow(TimerState())
    val timer: StateFlow<TimerState> = _timer.asStateFlow()

    private val _completedToday = MutableStateFlow(0)
    val completedToday: StateFlow<Int> = _completedToday.asStateFlow()

    private var timerJob: Job? = null

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _completedToday.value = repository.getCompletedToday()
        }
    }

    fun addTask(title: String, notes: String, quadrant: Quadrant, minutes: Int = 25) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTask(title, notes, quadrant, minutes)
            refreshStats()
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            repository.toggleComplete(task)
            refreshStats()
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
            refreshStats()
        }
    }

    fun moveTask(task: Task, newQuadrant: Quadrant) {
        viewModelScope.launch {
            repository.updateTask(task.copy(quadrant = newQuadrant))
        }
    }

    fun startTimer(minutes: Int = 25, task: Task? = null) {
        timerJob?.cancel()
        val total = minutes * 60
        _timer.value = TimerState(
            isRunning = true,
            remainingSeconds = total,
            totalSeconds = total,
            linkedTaskId = task?.id,
            linkedTaskTitle = task?.title ?: "Focus Session"
        )
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                delay(1000)
                _timer.value = _timer.value.copy(remainingSeconds = _timer.value.remainingSeconds - 1)
            }
            if (_timer.value.remainingSeconds <= 0) {
                _timer.value = _timer.value.copy(isRunning = false)
            }
        }
    }

    fun pauseTimer() {
        _timer.value = _timer.value.copy(isRunning = false)
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (_timer.value.remainingSeconds <= 0) return
        _timer.value = _timer.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                delay(1000)
                _timer.value = _timer.value.copy(remainingSeconds = _timer.value.remainingSeconds - 1)
            }
            if (_timer.value.remainingSeconds <= 0) {
                _timer.value = _timer.value.copy(isRunning = false)
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timer.value = TimerState()
    }

    fun setTimerMinutes(minutes: Int) {
        if (_timer.value.isRunning) return
        val secs = minutes * 60
        _timer.value = _timer.value.copy(remainingSeconds = secs, totalSeconds = secs)
    }
}
