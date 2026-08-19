package com.dailyapps.cascadeflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.cascadeflow.data.model.CascadeStats
import com.dailyapps.cascadeflow.data.model.EnergyLevel
import com.dailyapps.cascadeflow.data.model.FocusSession
import com.dailyapps.cascadeflow.data.model.Habit
import com.dailyapps.cascadeflow.data.model.Priority
import com.dailyapps.cascadeflow.data.model.Task
import com.dailyapps.cascadeflow.data.repository.CascadeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CascadeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CascadeRepository(application)

    val tasks: StateFlow<List<Task>> = repo.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTasks: StateFlow<List<Task>> = repo.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<Habit>> = repo.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSessions: StateFlow<List<FocusSession>> = repo.getRecentFocusSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _stats = MutableStateFlow(CascadeStats())
    val stats: StateFlow<CascadeStats> = _stats.asStateFlow()

    // Focus timer
    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _selectedMinutes = MutableStateFlow(25)
    val selectedMinutes: StateFlow<Int> = _selectedMinutes.asStateFlow()

    private var timerJob: Job? = null

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _stats.value = repo.getStats()
        }
    }

    fun addTask(title: String, description: String, priority: Priority, energy: EnergyLevel) {
        viewModelScope.launch {
            repo.addTask(
                Task(
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority,
                    energyRequired = energy
                )
            )
            refreshStats()
        }
    }

    fun toggleTaskComplete(task: Task) {
        viewModelScope.launch {
            val updated = if (task.isCompleted) {
                task.copy(isCompleted = false, completedAt = null)
            } else {
                task.copy(isCompleted = true, completedAt = System.currentTimeMillis())
            }
            repo.updateTask(updated)
            refreshStats()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.deleteTask(task)
            refreshStats()
        }
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            repo.addHabit(Habit(name = name.trim()))
            refreshStats()
        }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val updated = if (habit.isCompletedToday) {
                habit.copy(
                    isCompletedToday = false,
                    streak = (habit.streak - 1).coerceAtLeast(0),
                    lastCompletedDate = ""
                )
            } else {
                habit.copy(
                    isCompletedToday = true,
                    streak = habit.streak + 1,
                    lastCompletedDate = today
                )
            }
            repo.updateHabit(updated)
            refreshStats()
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repo.deleteHabit(habit)
            refreshStats()
        }
    }

    fun setFocusMinutes(minutes: Int) {
        if (!_isRunning.value) {
            _selectedMinutes.value = minutes
            _remainingSeconds.value = minutes * 60
        }
    }

    fun startTimer() {
        if (_isRunning.value) return
        if (_remainingSeconds.value <= 0) {
            _remainingSeconds.value = _selectedMinutes.value * 60
        }
        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _isRunning.value) {
                delay(1000)
                _remainingSeconds.value = _remainingSeconds.value - 1
            }
            if (_remainingSeconds.value <= 0) {
                _isRunning.value = false
                repo.addFocusSession(
                    FocusSession(durationMinutes = _selectedMinutes.value)
                )
                refreshStats()
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _remainingSeconds.value = _selectedMinutes.value * 60
    }

    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }
}
