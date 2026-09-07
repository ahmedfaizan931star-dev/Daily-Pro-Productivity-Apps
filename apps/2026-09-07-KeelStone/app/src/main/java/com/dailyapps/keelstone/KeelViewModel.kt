package com.dailyapps.keelstone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.keelstone.data.KeelRepository
import com.dailyapps.keelstone.domain.Carve
import com.dailyapps.keelstone.domain.KeelStone
import com.dailyapps.keelstone.domain.WeekState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

class KeelViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = KeelRepository(app)
    private val _state = MutableStateFlow(
        WeekState(KeelRepository.currentWeekLabel(), 20, KeelRepository.seedStones(), emptyList(), "")
    )
    val state: StateFlow<WeekState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.state.collect { loaded -> _state.value = loaded }
        }
    }

    private fun persist(next: WeekState) {
        _state.value = next
        viewModelScope.launch { repo.save(next) }
    }

    fun upsertStone(stone: KeelStone) {
        val stones = _state.value.stones.toMutableList()
        val idx = stones.indexOfFirst { it.id == stone.id }
        if (idx >= 0) stones[idx] = stone else stones.add(stone)
        persist(_state.value.copy(stones = stones))
    }

    fun removeStone(id: String) {
        persist(
            _state.value.copy(
                stones = _state.value.stones.filterNot { it.id == id },
                carves = _state.value.carves.filterNot { it.stoneId == id }
            )
        )
    }

    fun toggleDone(id: String) {
        persist(
            _state.value.copy(
                stones = _state.value.stones.map {
                    if (it.id == id) it.copy(done = !it.done) else it
                }
            )
        )
    }

    fun addCarve(stoneId: String, minutes: Int, note: String) {
        val day = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val carve = Carve(UUID.randomUUID().toString(), stoneId, day, minutes, note)
        val hours = minutes / 60f
        persist(
            _state.value.copy(
                carves = listOf(carve) + _state.value.carves,
                stones = _state.value.stones.map {
                    if (it.id == stoneId) it.copy(hoursLogged = it.hoursLogged + hours) else it
                }
            )
        )
    }

    fun setCapacity(hours: Int) {
        persist(_state.value.copy(capacityHours = hours.coerceIn(4, 60)))
    }

    fun setReview(note: String) {
        persist(_state.value.copy(reviewNote = note))
    }

    fun newStone(): KeelStone =
        KeelStone(UUID.randomUUID().toString(), "", "", 4, 0f, false)
}
