package com.dailyapps.pulseforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.pulseforge.data.model.FocusMode
import com.dailyapps.pulseforge.data.model.Habit
import com.dailyapps.pulseforge.data.model.Priority
import com.dailyapps.pulseforge.data.repository.PulseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val habits: List<Habit> = emptyList(),
    val completedHabitIds: Set<Long> = emptySet(),
    val priorities: List<Priority> = emptyList(),
    val todayFocusMinutes: Int = 0,
    val completedHabitsCount: Int = 0,
    val totalHabits: Int = 0,
    val completedPriorities: Int = 0
)

data class TimerUiState(
    val selectedMode: FocusMode = FocusMode.CLASSIC,
    val isRunning: Boolean = false,
    val isBreak: Boolean = false,
    val remainingSeconds: Int = FocusMode.CLASSIC.workMinutes * 60,
    val totalSeconds: Int = FocusMode.CLASSIC.workMinutes * 60,
    val sessionsCompletedToday: Int = 0,
    val customWorkMinutes: Int = 25,
    val customBreakMinutes: Int = 5
)

class PulseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PulseRepository(application)

    private val _dashboard = MutableStateFlow(DashboardUiState())
    val dashboard: StateFlow<DashboardUiState> = _dashboard.asStateFlow()

    private val _timer = MutableStateFlow(TimerUiState())
    val timer: StateFlow<TimerUiState> = _timer.asStateFlow()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _habitCompletions = MutableStateFlow<Set<Long>>(emptySet())
    val habitCompletions: StateFlow<Set<Long>> = _habitCompletions.asStateFlow()

    private val _priorities = MutableStateFlow<List<Priority>>(emptyList())
    val priorities: StateFlow<List<Priority>> = _priorities.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                repository.getHabits(),
                repository.getTodayCompletions(),
                repository.getTodayPriorities()
            ) { habits, completions, priorities ->
                Triple(habits, completions, priorities)
            }.collect { (habits, completions, priorities) ->
                val completedIds = completions.map { it.habitId }.toSet()
                val focusMins = repository.getTodayFocusMinutes()
                _habits.value = habits
                _habitCompletions.value = completedIds
                _priorities.value = priorities
                _dashboard.value = DashboardUiState(
                    habits = habits,
                    completedHabitIds = completedIds,
                    priorities = priorities,
                    todayFocusMinutes = focusMins,
                    completedHabitsCount = completedIds.size,
                    totalHabits = habits.size,
                    completedPriorities = priorities.count { it.isCompleted }
                )
            }
        }
    }

    fun selectMode(mode: FocusMode) {
        if (_timer.value.isRunning) return
        val work = if (mode == FocusMode.CUSTOM) _timer.value.customWorkMinutes else mode.workMinutes
        _timer.update {
            it.copy(
                selectedMode = mode,
                isBreak = false,
                remainingSeconds = work * 60,
                totalSeconds = work * 60
            )
        }
    }

    fun updateCustomMinutes(work: Int, breakMin: Int) {
        _timer.update {
            it.copy(
                customWorkMinutes = work.coerceIn(5, 120),
                customBreakMinutes = breakMin.coerceIn(1, 30)
            )
        }
        if (_timer.value.selectedMode == FocusMode.CUSTOM && !_timer.value.isRunning) {
            selectMode(FocusMode.CUSTOM)
        }
    }

    fun startTimer() {
        if (_timer.value.isRunning) return
        _timer.update { it.copy(isRunning = true) }
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingSeconds > 0 && _timer.value.isRunning) {
                delay(1000)
                _timer.update { state ->
                    if (state.isRunning) state.copy(remainingSeconds = state.remainingSeconds - 1)
                    else state
                }
            }
            if (_timer.value.remainingSeconds <= 0 && _timer.value.isRunning) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timer.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val mode = _timer.value.selectedMode
        val work = if (mode == FocusMode.CUSTOM) _timer.value.customWorkMinutes else mode.workMinutes
        _timer.update {
            it.copy(
                isRunning = false,
                isBreak = false,
                remainingSeconds = work * 60,
                totalSeconds = work * 60
            )
        }
    }

    private fun onTimerFinished() {
        val state = _timer.value
        viewModelScope.launch {
            if (!state.isBreak) {
                val minutes = state.totalSeconds / 60
                repository.logFocusSession(minutes, state.selectedMode, true)
                _dashboard.update { it.copy(todayFocusMinutes = repository.getTodayFocusMinutes()) }
                val breakMin = if (state.selectedMode == FocusMode.CUSTOM)
                    state.customBreakMinutes else state.selectedMode.breakMinutes
                _timer.update {
                    it.copy(
                        isRunning = false,
                        isBreak = true,
                        remainingSeconds = breakMin * 60,
                        totalSeconds = breakMin * 60,
                        sessionsCompletedToday = it.sessionsCompletedToday + 1
                    )
                }
            } else {
                val work = if (state.selectedMode == FocusMode.CUSTOM)
                    state.customWorkMinutes else state.selectedMode.workMinutes
                _timer.update {
                    it.copy(
                        isRunning = false,
                        isBreak = false,
                        remainingSeconds = work * 60,
                        totalSeconds = work * 60
                    )
                }
            }
        }
    }

    fun skipToBreakOrWork() {
        timerJob?.cancel()
        onTimerFinished()
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addHabit(title.trim())
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleHabit(habitId: Long) {
        val currently = _habitCompletions.value.contains(habitId)
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, currently)
        }
    }

    fun addPriority(title: String) {
        if (title.isBlank()) return
        val nextIndex = _priorities.value.size
        if (nextIndex >= 5) return
        viewModelScope.launch {
            repository.addPriority(title.trim(), nextIndex)
        }
    }

    fun togglePriority(priority: Priority) {
        viewModelScope.launch {
            repository.togglePriority(priority)
        }
    }

    fun deletePriority(priority: Priority) {
        viewModelScope.launch {
            repository.deletePriority(priority)
        }
    }
}
