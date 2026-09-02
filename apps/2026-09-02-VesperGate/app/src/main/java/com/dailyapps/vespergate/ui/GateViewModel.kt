package com.dailyapps.vespergate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dailyapps.vespergate.data.AppDatabase
import com.dailyapps.vespergate.data.EveningEntity
import com.dailyapps.vespergate.data.LoopEntity
import java.time.LocalDate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GateUiState(
    val tonight: EveningEntity = EveningEntity(dateKey = LocalDate.now().toString()),
    val loops: List<LoopEntity> = emptyList(),
    val evenings: List<EveningEntity> = emptyList()
)

class GateViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val eveningDao = db.eveningDao()
    private val loopDao = db.loopDao()
    private val today = LocalDate.now().toString()

    val state = combine(
        eveningDao.observeAll(),
        loopDao.observeAll()
    ) { evenings, loops ->
        val tonight = evenings.firstOrNull { it.dateKey == today }
            ?: EveningEntity(dateKey = today)
        GateUiState(tonight = tonight, loops = loops, evenings = evenings)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GateUiState())

    private suspend fun current(): EveningEntity =
        eveningDao.get(today) ?: EveningEntity(dateKey = today)

    fun saveNote(note: String) = viewModelScope.launch {
        eveningDao.upsert(current().copy(note = note))
    }

    fun saveIntention(intention: String) = viewModelScope.launch {
        eveningDao.upsert(current().copy(intention = intention))
    }

    fun toggleRitual(bit: Int) = viewModelScope.launch {
        val e = current()
        val next = e.ritualsDone xor (1 shl bit)
        eveningDao.upsert(e.copy(ritualsDone = next))
    }

    fun setScore(score: Int) = viewModelScope.launch {
        eveningDao.upsert(current().copy(score = score))
    }

    fun sealTonight() = viewModelScope.launch {
        eveningDao.upsert(current().copy(sealed = true))
    }

    fun addLoop(title: String) = viewModelScope.launch {
        if (title.isBlank()) return@launch
        loopDao.insert(LoopEntity(title = title.trim()))
    }

    fun parkLoop(loop: LoopEntity) = viewModelScope.launch {
        loopDao.update(loop.copy(status = "parked"))
    }

    fun closeLoop(loop: LoopEntity) = viewModelScope.launch {
        loopDao.update(loop.copy(status = "closed"))
    }

    fun deleteLoop(loop: LoopEntity) = viewModelScope.launch {
        loopDao.delete(loop)
    }

    companion object {
        fun factory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GateViewModel(app) as T
                }
            }
    }
}
