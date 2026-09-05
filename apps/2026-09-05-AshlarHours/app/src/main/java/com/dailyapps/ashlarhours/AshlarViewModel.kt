package com.dailyapps.ashlarhours

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.ashlarhours.data.PlanStore
import com.dailyapps.ashlarhours.domain.HourStone
import com.dailyapps.ashlarhours.domain.Insights
import com.dailyapps.ashlarhours.domain.StoneKind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

data class AshlarUiState(
    val stones: List<HourStone> = emptyList(),
    val selectedHour: Int? = null,
    val draftTitle: String = "",
    val draftKind: StoneKind = StoneKind.DEEP,
    val deepTarget: Int = 4,
    val insights: Insights = Insights(0, 0, 0, 0, 0, 0, ""),
    val todayLabel: String = ""
)

class AshlarViewModel(application: Application) : AndroidViewModel(application) {

    private val store = PlanStore(application)
    private val _state = MutableStateFlow(AshlarUiState())
    val state: StateFlow<AshlarUiState> = _state

    init {
        viewModelScope.launch {
            combine(store.payload, store.deepTarget) { payload, target ->
                payload to target
            }.collect { (payload, target) ->
                val stones = PlanStore.decode(payload).ifEmpty { seed() }
                _state.update {
                    it.copy(
                        stones = stones.sortedBy { s -> s.hour },
                        deepTarget = target,
                        insights = compute(stones, target),
                        todayLabel = LocalDate.now().toString()
                    )
                }
            }
        }
    }

    fun selectHour(hour: Int?) {
        val existing = _state.value.stones.find { it.hour == hour }
        _state.update {
            it.copy(
                selectedHour = hour,
                draftTitle = existing?.title.orEmpty(),
                draftKind = existing?.kind ?: StoneKind.DEEP
            )
        }
    }

    fun setDraftTitle(value: String) {
        _state.update { it.copy(draftTitle = value) }
    }

    fun setDraftKind(kind: StoneKind) {
        _state.update { it.copy(draftKind = kind) }
    }

    fun placeStone() {
        val hour = _state.value.selectedHour ?: return
        val title = _state.value.draftTitle.ifBlank { _state.value.draftKind.label }
        val next = _state.value.stones.toMutableList()
        val idx = next.indexOfFirst { it.hour == hour }
        val stone = HourStone(
            id = if (idx >= 0) next[idx].id else UUID.randomUUID().toString(),
            hour = hour,
            kind = _state.value.draftKind,
            title = title,
            done = if (idx >= 0) next[idx].done else false
        )
        if (idx >= 0) next[idx] = stone else next.add(stone)
        persist(next)
        selectHour(null)
    }

    fun toggleDone(id: String) {
        persist(_state.value.stones.map { if (it.id == id) it.copy(done = !it.done) else it })
    }

    fun clearHour(hour: Int) {
        persist(_state.value.stones.filterNot { it.hour == hour })
        selectHour(null)
    }

    fun applyTemplate(name: String) {
        val template = when (name) {
            "maker" -> listOf(
                8 to (StoneKind.RECOVERY to "Walk + coffee"),
                9 to (StoneKind.DEEP to "Maker block A"),
                10 to (StoneKind.DEEP to "Maker block A"),
                11 to (StoneKind.DEEP to "Maker block A"),
                13 to (StoneKind.ADMIN to "Inbox sweep"),
                14 to (StoneKind.DEEP to "Maker block B"),
                15 to (StoneKind.DEEP to "Maker block B"),
                16 to (StoneKind.SOCIAL to "Collab window"),
                17 to (StoneKind.RECOVERY to "Shutdown")
            )
            "manager" -> listOf(
                8 to (StoneKind.ADMIN to "Priorities"),
                9 to (StoneKind.SOCIAL to "1:1s"),
                10 to (StoneKind.SOCIAL to "Staffing"),
                11 to (StoneKind.DEEP to "Strategy memo"),
                13 to (StoneKind.ADMIN to "Decisions"),
                14 to (StoneKind.SOCIAL to "Reviews"),
                15 to (StoneKind.ADMIN to "Ops"),
                16 to (StoneKind.RECOVERY to "Buffer")
            )
            else -> listOf(
                9 to (StoneKind.DEEP to "Focus 1"),
                10 to (StoneKind.DEEP to "Focus 2"),
                14 to (StoneKind.ADMIN to "Ops"),
                16 to (StoneKind.RECOVERY to "Reset")
            )
        }
        val stones = template.map { (hour, pair) ->
            HourStone(UUID.randomUUID().toString(), hour, pair.first, pair.second)
        }
        persist(stones)
    }

    fun setDeepTarget(hours: Int) {
        viewModelScope.launch { store.setDeepTarget(hours) }
    }

    private fun persist(stones: List<HourStone>) {
        viewModelScope.launch { store.save(PlanStore.encode(stones)) }
    }

    private fun seed(): List<HourStone> = listOf(
        HourStone("s1", 9, StoneKind.DEEP, "First mason block"),
        HourStone("s2", 10, StoneKind.DEEP, "Second mason block"),
        HourStone("s3", 14, StoneKind.ADMIN, "Ops mortar"),
        HourStone("s4", 16, StoneKind.RECOVERY, "Reset stone")
    )

    private fun compute(stones: List<HourStone>, target: Int): Insights {
        fun count(kind: StoneKind) = stones.count { it.kind == kind }
        val deep = count(StoneKind.DEEP)
        val note = when {
            deep >= target -> "Deep-work masonry is holding. Protect the joints."
            deep == 0 -> "No load-bearing stones today. Place at least one deep block."
            else -> "Need ${target - deep} more deep hour${if (target - deep == 1) "" else "s"} to hit the weekly-day target."
        }
        return Insights(
            deepHours = deep,
            adminHours = count(StoneKind.ADMIN),
            recoveryHours = count(StoneKind.RECOVERY),
            socialHours = count(StoneKind.SOCIAL),
            completed = stones.count { it.done },
            planned = stones.size,
            balanceNote = note
        )
    }
}
