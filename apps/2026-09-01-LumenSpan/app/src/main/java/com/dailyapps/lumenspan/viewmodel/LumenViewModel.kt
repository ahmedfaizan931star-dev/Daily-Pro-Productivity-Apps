package com.dailyapps.lumenspan.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.lumenspan.data.EnergyBand
import com.dailyapps.lumenspan.data.EnergyCheckin
import com.dailyapps.lumenspan.data.LumenDatabase
import com.dailyapps.lumenspan.data.SpanTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class LumenUiState(
    val tasks: List<SpanTask> = emptyList(),
    val checkins: List<EnergyCheckin> = emptyList(),
    val todayEpoch: Long = todayStart()
)

fun todayStart(): Long =
    LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

class LumenViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = LumenDatabase.get(app).dao()

    val uiState: StateFlow<LumenUiState> = combine(
        dao.observeTasks(),
        dao.observeCheckins()
    ) { tasks, checkins ->
        LumenUiState(tasks = tasks, checkins = checkins, todayEpoch = todayStart())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LumenUiState())

    fun addTask(title: String, band: EnergyBand, minutes: Int) {
        if (title.isBlank()) return
        viewModelScope.launch {
            dao.insertTask(
                SpanTask(
                    title = title.trim(),
                    band = band,
                    minutes = minutes.coerceIn(5, 240),
                    dayEpoch = todayStart()
                )
            )
        }
    }

    fun toggle(task: SpanTask) {
        viewModelScope.launch { dao.updateTask(task.copy(done = !task.done)) }
    }

    fun remove(task: SpanTask) {
        viewModelScope.launch { dao.deleteTask(task) }
    }

    fun checkIn(score: Int, note: String) {
        viewModelScope.launch {
            dao.insertCheckin(
                EnergyCheckin(
                    score = score.coerceIn(1, 5),
                    note = note.trim(),
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
