package com.dailyapps.zenithfocus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.zenithfocus.data.model.EnergyLevel
import com.dailyapps.zenithfocus.data.model.FocusSession
import com.dailyapps.zenithfocus.data.model.MatrixQuadrant
import com.dailyapps.zenithfocus.data.model.Reflection
import com.dailyapps.zenithfocus.data.model.Task
import com.dailyapps.zenithfocus.data.repository.ZenithRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val focusMinutesToday: Int = 0,
    val completedTasksToday: Int = 0,
    val openTasks: List<Task> = emptyList(),
    val todayReflection: Reflection? = null,
    val currentEnergy: EnergyLevel? = null
)

data class TimerUiState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val remainingSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val mode: String = "Pomodoro",
    val progress: Float = 1f
)

class ZenithViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ZenithRepository(application)

    val allTasks: StateFlow<List<Task>> = repo.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val openTasks: StateFlow<List<Task>> = repo.getOpenTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val focusMinutesToday: StateFlow<Int> = repo.focusMinutesToday()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedToday: StateFlow<Int> = repo.completedTodayCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayReflection: StateFlow<Reflection?> = repo.getTodayReflection()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val homeState: StateFlow<HomeUiState> = combine(
        focusMinutesToday, completedToday, openTasks, todayReflection
    ) { minutes, completed, tasks, reflection ->
        HomeUiState(
            focusMinutesToday = minutes,
            completedTasksToday = completed,
            openTasks = tasks.take(5),
            todayReflection = reflection,
            currentEnergy = reflection?.energyMorning ?: reflection?.energyAfternoon ?: reflection?.energyEvening
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    // Timer
    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun setTimerMode(mode: String, minutes: Int) {
        timerJob?.cancel()
        val secs = minutes * 60
        _timerState.value = TimerUiState(
            isRunning = false,
            isPaused = false,
            remainingSeconds = secs,
            totalSeconds = secs,
            mode = mode,
            progress = 1f
        )
    }

    fun startTimer() {
        if (_timerState.value.isRunning) return
        _timerState.value = _timerState.value.copy(isRunning = true, isPaused = false)
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000)
                val current = _timerState.value
                if (!current.isRunning) break
                val next = current.remainingSeconds - 1
                _timerState.value = current.copy(
                    remainingSeconds = next,
                    progress = next.toFloat() / current.totalSeconds
                )
            }
            if (_timerState.value.remainingSeconds <= 0) {
                onTimerComplete()
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isRunning = false, isPaused = true)
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        val total = _timerState.value.totalSeconds
        _timerState.value = _timerState.value.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = total,
            progress = 1f
        )
    }

    private fun onTimerComplete() {
        viewModelScope.launch {
            val minutes = _timerState.value.totalSeconds / 60
            repo.logFocusSession(minutes, _timerState.value.mode)
            _timerState.value = _timerState.value.copy(isRunning = false, remainingSeconds = 0, progress = 0f)
        }
    }

    // Tasks
    fun addTask(title: String, notes: String, quadrant: MatrixQuadrant, energy: EnergyLevel) {
        viewModelScope.launch {
            repo.addTask(title, notes, quadrant, energy)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repo.toggleTaskCompleted(task)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repo.deleteTask(id)
        }
    }

    // Reflection
    fun saveReflection(
        energyMorning: EnergyLevel?,
        energyAfternoon: EnergyLevel?,
        energyEvening: EnergyLevel?,
        wins: String,
        challenges: String,
        gratitude: String,
        intention: String
    ) {
        viewModelScope.launch {
            val existing = todayReflection.value
            val reflection = Reflection(
                id = existing?.id ?: 0,
                dateKey = repo.todayKey(),
                energyMorning = energyMorning ?: existing?.energyMorning,
                energyAfternoon = energyAfternoon ?: existing?.energyAfternoon,
                energyEvening = energyEvening ?: existing?.energyEvening,
                wins = wins.ifBlank { existing?.wins ?: "" },
                challenges = challenges.ifBlank { existing?.challenges ?: "" },
                gratitude = gratitude.ifBlank { existing?.gratitude ?: "" },
                tomorrowIntention = intention.ifBlank { existing?.tomorrowIntention ?: "" }
            )
            repo.saveReflection(reflection)
        }
    }

    fun setEnergy(period: String, level: EnergyLevel) {
        viewModelScope.launch {
            val existing = todayReflection.value
            val reflection = Reflection(
                id = existing?.id ?: 0,
                dateKey = repo.todayKey(),
                energyMorning = if (period == "morning") level else existing?.energyMorning,
                energyAfternoon = if (period == "afternoon") level else existing?.energyAfternoon,
                energyEvening = if (period == "evening") level else existing?.energyEvening,
                wins = existing?.wins ?: "",
                challenges = existing?.challenges ?: "",
                gratitude = existing?.gratitude ?: "",
                tomorrowIntention = existing?.tomorrowIntention ?: ""
            )
            repo.saveReflection(reflection)
        }
    }
}
