package com.dailyapps.vortexvault.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.vortexvault.data.local.AppDatabase
import com.dailyapps.vortexvault.data.model.FocusSession
import com.dailyapps.vortexvault.data.model.Habit
import com.dailyapps.vortexvault.data.model.Task
import com.dailyapps.vortexvault.data.repository.VortexRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TimerState(
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val isBreak: Boolean = false
)

class VortexViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VortexRepository(AppDatabase.getInstance(application))

    val habits: StateFlow<List<Habit>> = repository.habits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = repository.tasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayFocusMinutes: StateFlow<Int> = repository.todayFocusMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentSessions: StateFlow<List<FocusSession>> = repository.recentSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _timer = MutableStateFlow(TimerState())
    val timer: StateFlow<TimerState> = _timer.asStateFlow()

    private var timerJob: Job? = null

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addHabit(title) }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch { repository.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repository.deleteHabit(id) }
    }

    fun addTask(title: String, priority: Int = 2) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addTask(title, priority) }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch { repository.toggleTask(task) }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch { repository.deleteTask(id) }
    }

    fun setTimerDuration(minutes: Int) {
        if (_timer.value.isRunning) return
        val secs = minutes * 60
        _timer.value = TimerState(totalSeconds = secs, remainingSeconds = secs)
    }

    fun startTimer() {
        if (_timer.value.isRunning) return
        _timer.value = _timer.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                delay(1000)
                _timer.value = _timer.value.copy(
                    remainingSeconds = _timer.value.remainingSeconds - 1
                )
            }
            if (_timer.value.remainingSeconds <= 0) {
                val minutes = _timer.value.totalSeconds / 60
                if (!_timer.value.isBreak) {
                    repository.saveFocusSession(minutes)
                }
                _timer.value = _timer.value.copy(isRunning = false)
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timer.value = _timer.value.copy(isRunning = false)
    }

    fun resetTimer() {
        timerJob?.cancel()
        val total = _timer.value.totalSeconds
        _timer.value = TimerState(totalSeconds = total, remainingSeconds = total)
    }
}
