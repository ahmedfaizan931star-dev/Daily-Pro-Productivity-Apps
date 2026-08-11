package com.dailyapps.cadencecore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.cadencecore.data.model.FocusSession
import com.dailyapps.cadencecore.data.model.HabitWithProgress
import com.dailyapps.cadencecore.data.model.Reflection
import com.dailyapps.cadencecore.data.repository.CadenceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CadenceUiState(
    val habits: List<HabitWithProgress> = emptyList(),
    val focusMinutesToday: Int = 0,
    val sessionsToday: List<FocusSession> = emptyList(),
    val reflection: Reflection? = null,
    val isTimerRunning: Boolean = false,
    val timerSecondsLeft: Int = 25 * 60,
    val selectedDurationMinutes: Int = 25,
    val mood: Int = 3,
    val energy: Int = 3,
    val reflectionText: String = ""
)

class CadenceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CadenceRepository(application)

    private val _timerSeconds = MutableStateFlow(25 * 60)
    private val _isRunning = MutableStateFlow(false)
    private val _selectedMinutes = MutableStateFlow(25)
    private val _mood = MutableStateFlow(3)
    private val _energy = MutableStateFlow(3)
    private val _reflectionText = MutableStateFlow("")

    private var timerJob: Job? = null

    val uiState: StateFlow<CadenceUiState> = combine(
        repository.getHabitsWithProgress(),
        repository.getFocusMinutesToday(),
        repository.getSessionsToday(),
        repository.getReflectionToday(),
        _timerSeconds,
        _isRunning,
        _selectedMinutes,
        _mood,
        _energy,
        _reflectionText
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        CadenceUiState(
            habits = values[0] as List<HabitWithProgress>,
            focusMinutesToday = values[1] as Int,
            sessionsToday = values[2] as List<FocusSession>,
            reflection = values[3] as Reflection?,
            timerSecondsLeft = values[4] as Int,
            isTimerRunning = values[5] as Boolean,
            selectedDurationMinutes = values[6] as Int,
            mood = values[7] as Int,
            energy = values[8] as Int,
            reflectionText = values[9] as String
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CadenceUiState()
    )

    init {
        viewModelScope.launch {
            repository.seedIfEmpty()
        }
        viewModelScope.launch {
            repository.getReflectionToday().collect { ref ->
                if (ref != null) {
                    _mood.value = ref.mood
                    _energy.value = ref.energy
                    _reflectionText.value = ref.content
                }
            }
        }
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addHabit(title.trim()) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repository.deleteHabit(id) }
    }

    fun logHabit(habitId: Long) {
        viewModelScope.launch { repository.logHabit(habitId) }
    }

    fun setDuration(minutes: Int) {
        if (_isRunning.value) return
        _selectedMinutes.value = minutes
        _timerSeconds.value = minutes * 60
    }

    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0 && _isRunning.value) {
                delay(1000)
                _timerSeconds.value = _timerSeconds.value - 1
            }
            if (_timerSeconds.value == 0) {
                _isRunning.value = false
                repository.addFocusSession(_selectedMinutes.value)
                _timerSeconds.value = _selectedMinutes.value * 60
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerSeconds.value = _selectedMinutes.value * 60
    }

    fun completeSessionEarly() {
        val elapsed = _selectedMinutes.value * 60 - _timerSeconds.value
        val minutes = (elapsed / 60).coerceAtLeast(1)
        pauseTimer()
        viewModelScope.launch {
            repository.addFocusSession(minutes)
        }
        _timerSeconds.value = _selectedMinutes.value * 60
    }

    fun setMood(value: Int) { _mood.value = value.coerceIn(1, 5) }
    fun setEnergy(value: Int) { _energy.value = value.coerceIn(1, 5) }
    fun setReflectionText(text: String) { _reflectionText.value = text }

    fun saveReflection() {
        viewModelScope.launch {
            repository.saveReflection(_mood.value, _energy.value, _reflectionText.value)
        }
    }
}
