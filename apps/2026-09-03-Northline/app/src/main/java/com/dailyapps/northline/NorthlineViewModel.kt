package com.dailyapps.northline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dailyapps.northline.data.NorthlineStore
import com.dailyapps.northline.domain.Commitment
import com.dailyapps.northline.domain.DayPlan
import com.dailyapps.northline.domain.EnergyBand
import com.dailyapps.northline.domain.Friction
import com.dailyapps.northline.domain.NorthlineState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class NorthlineViewModel(app: Application) : AndroidViewModel(app) {
    private val store = NorthlineStore(app)
    private val _state = MutableStateFlow(buildState())
    val state: StateFlow<NorthlineState> = _state.asStateFlow()

    fun setNorthStar(text: String) = mutate { it.copy(northStar = text) }

    fun addCommitment(title: String, leverage: Int, energy: EnergyBand, friction: Friction) {
        if (title.isBlank()) return
        val item = Commitment(
            id = System.currentTimeMillis().toString(),
            title = title.trim(),
            leverage = leverage.coerceIn(1, 5),
            energy = energy,
            friction = friction,
            done = false,
            createdAt = System.currentTimeMillis()
        )
        mutate { plan ->
            plan.copy(commitments = (plan.commitments + item).sortedByDescending { it.leverage })
        }
    }

    fun toggle(id: String) = mutate { plan ->
        plan.copy(commitments = plan.commitments.map {
            if (it.id == id) it.copy(done = !it.done) else it
        })
    }

    fun remove(id: String) = mutate { plan ->
        plan.copy(commitments = plan.commitments.filterNot { it.id == id })
    }

    fun closeDay() = mutate { plan ->
        val done = plan.commitments.count { it.done }
        val total = plan.commitments.size.coerceAtLeast(1)
        val leverageDone = plan.commitments.filter { it.done }.sumOf { it.leverage }
        val leverageTotal = plan.commitments.sumOf { it.leverage }.coerceAtLeast(1)
        val score = ((done.toFloat() / total) * 40 + (leverageDone.toFloat() / leverageTotal) * 60).roundToInt()
        plan.copy(closed = true, alignmentScore = score)
    }

    fun reopen() = mutate { it.copy(closed = false) }

    private fun mutate(block: (DayPlan) -> DayPlan) {
        val next = block(_state.value.today)
        store.saveToday(next)
        if (next.closed) {
            val hist = store.loadHistory().filterNot { it.dateKey == next.dateKey } + next
            store.saveHistory(hist)
        }
        _state.value = buildState(next)
    }

    private fun buildState(today: DayPlan = store.loadToday()): NorthlineState {
        val history = store.loadHistory()
        val week = history.takeLast(7)
        val wins = week.count { it.alignmentScore >= 70 }
        val streak = computeStreak(history + listOf(today).filter { it.closed && history.none { h -> h.dateKey == it.dateKey } })
        return NorthlineState(today, history.takeLast(14).reversed(), wins, streak)
    }

    private fun computeStreak(history: List<DayPlan>): Int {
        var count = 0
        for (plan in history.sortedByDescending { it.dateKey }) {
            if (plan.closed && plan.alignmentScore >= 60) count++ else break
        }
        return count
    }
}
