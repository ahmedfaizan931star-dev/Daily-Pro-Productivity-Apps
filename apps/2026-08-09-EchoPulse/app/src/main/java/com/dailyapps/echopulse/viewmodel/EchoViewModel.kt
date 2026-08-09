package com.dailyapps.echopulse.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.echopulse.data.local.AppDatabase
import com.dailyapps.echopulse.data.model.FocusSession
import com.dailyapps.echopulse.data.model.Habit
import com.dailyapps.echopulse.data.model.Reflection
import com.dailyapps.echopulse.data.repository.EchoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class EchoUiState(
    val habits: List<Habit> = emptyList(),
    val todaySessions: List<FocusSession> = emptyList(),
    val reflections: List<Reflection> = emptyList(),
    val todayReflection: Reflection? = null,
    val focusMinutesToday: Int = 0,
    val habitsCompleted: Int = 0,
    val habitsTotal: Int = 0,
    val pulseScore: Int = 0,
    val isTimerRunning: Boolean = false,
    val remainingSeconds: Int = 0,
    val selectedDurationMinutes: Int = 25
)

class EchoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = EchoRepository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(EchoUiState())
    val uiState: StateFlow<EchoUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    private val startOfDay: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    init {
        viewModelScope.launch {
            combine(
                repo.getHabits(),
                repo.getTodayFocusSessions(startOfDay),
                repo.getReflections(),
                repo.getTodayReflection()
            ) { habits, sessions, reflections, todayRef ->
                val completed = habits.count { it.completedToday }
                val focusMins = sessions.sumOf { it.durationMinutes }
                val pulse = calculatePulse(completed, habits.size, focusMins, todayRef != null)
                EchoUiState(
                    habits = habits,
                    todaySessions = sessions,
                    reflections = reflections,
                    todayReflection = todayRef,
                    focusMinutesToday = focusMins,
                    habitsCompleted = completed,
                    habitsTotal = habits.size,
                    pulseScore = pulse,
                    isTimerRunning = _uiState.value.isTimerRunning,
                    remainingSeconds = _uiState.value.remainingSeconds,
                    selectedDurationMinutes = _uiState.value.selectedDurationMinutes
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun calculatePulse(completed: Int, total: Int, focusMins: Int, hasReflection: Boolean): Int {
        val habitScore = if (total == 0) 30 else ((completed.toFloat() / total) * 40).toInt()
        val focusScore = (focusMins.coerceAtMost(120) / 120f * 40).toInt()
        val reflectScore = if (hasReflection) 20 else 0
        return (habitScore + focusScore + reflectScore).coerceIn(0, 100)
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repo.addHabit(title.trim())
        }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            repo.toggleHabit(habit)
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch {
            repo.deleteHabit(id)
        }
    }

    fun setDuration(minutes: Int) {
        if (!_uiState.value.isTimerRunning) {
            _uiState.value = _uiState.value.copy(
                selectedDurationMinutes = minutes,
                remainingSeconds = minutes * 60
            )
        }
    }

    fun startTimer() {
        if (_uiState.value.isTimerRunning) return
        val seconds = _uiState.value.selectedDurationMinutes * 60
        _uiState.value = _uiState.value.copy(
            isTimerRunning = true,
            remainingSeconds = seconds
        )
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = seconds
            while (remaining > 0) {
                delay(1000)
                remaining--
                _uiState.value = _uiState.value.copy(remainingSeconds = remaining)
            }
            // completed
            _uiState.value = _uiState.value.copy(isTimerRunning = false, remainingSeconds = 0)
            repo.addFocusSession(_uiState.value.selectedDurationMinutes)
        }
    }

    fun pauseOrResetTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isTimerRunning = false,
            remainingSeconds = _uiState.value.selectedDurationMinutes * 60
        )
    }

    fun saveReflection(content: String, energy: Int, mood: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repo.saveReflection(content.trim(), energy, mood)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
