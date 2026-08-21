package com.dailyapps.prismflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.prismflow.data.model.EnergyLog
import com.dailyapps.prismflow.data.model.FocusSession
import com.dailyapps.prismflow.data.model.Habit
import com.dailyapps.prismflow.data.model.PriorityQuadrant
import com.dailyapps.prismflow.data.model.Task
import com.dailyapps.prismflow.data.repository.PrismRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrismViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PrismRepository(application)

    val tasks: StateFlow<List<Task>> = repository.tasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<Habit>> = repository.habits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val energyLogs: StateFlow<List<EnergyLog>> = repository.energyLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _focusSession = MutableStateFlow(FocusSession())
    val focusSession: StateFlow<FocusSession> = _focusSession.asStateFlow()

    private var timerJob: Job? = null

    fun addTask(title: String, notes: String = "", quadrant: PriorityQuadrant = PriorityQuadrant.URGENT_IMPORTANT) {
        viewModelScope.launch {
            repository.addTask(title, notes, quadrant)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.toggleTask(task)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            repository.addHabit(name)
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

    fun logEnergy(level: Int, note: String = "") {
        viewModelScope.launch {
            repository.logEnergy(level, note)
        }
    }

    fun startFocus(minutes: Int = 25) {
        timerJob?.cancel()
        _focusSession.value = FocusSession(
            durationMinutes = minutes,
            isRunning = true,
            remainingSeconds = minutes * 60
        )
        timerJob = viewModelScope.launch {
            while (_focusSession.value.remainingSeconds > 0 && _focusSession.value.isRunning) {
                delay(1000)
                _focusSession.value = _focusSession.value.copy(
                    remainingSeconds = _focusSession.value.remainingSeconds - 1
                )
            }
            if (_focusSession.value.remainingSeconds <= 0) {
                _focusSession.value = _focusSession.value.copy(isRunning = false)
            }
        }
    }

    fun pauseFocus() {
        timerJob?.cancel()
        _focusSession.value = _focusSession.value.copy(isRunning = false)
    }

    fun resetFocus() {
        timerJob?.cancel()
        _focusSession.value = FocusSession()
    }
}
