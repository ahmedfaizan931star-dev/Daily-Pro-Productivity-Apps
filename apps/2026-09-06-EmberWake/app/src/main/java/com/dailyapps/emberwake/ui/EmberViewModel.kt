package com.dailyapps.emberwake.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.emberwake.data.EmberStore
import com.dailyapps.emberwake.domain.Commitment
import com.dailyapps.emberwake.domain.DayLog
import com.dailyapps.emberwake.domain.EmberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class EmberViewModel(app: Application) : AndroidViewModel(app) {
    private val store = EmberStore(app)
    private val _ui = MutableStateFlow(EmberState(date = LocalDate.now().toString()))
    val ui: StateFlow<EmberState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            store.state.collect { loaded ->
                _ui.value = loaded.copy(draft = _ui.value.draft)
            }
        }
    }

    fun setEnergy(value: Int) = mutate { copy(energy = value.coerceIn(1, 10)) }

    fun toggleWake(id: String) = mutate {
        copy(wake = wake.map { if (it.id == id) it.copy(done = !it.done) else it })
    }

    fun toggleShut(id: String) = mutate {
        copy(shutdown = shutdown.map { if (it.id == id) it.copy(done = !it.done) else it })
    }

    fun setDraft(text: String) = _ui.update { it.copy(draft = text) }

    fun addCommitment() {
        val title = _ui.value.draft.trim()
        if (title.isEmpty()) return
        mutate {
            copy(
                commitments = commitments + Commitment(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    energyCost = if (energy >= 7) 3 else 2
                ),
                draft = ""
            )
        }
    }

    fun toggleCommit(id: String) = mutate {
        copy(commitments = commitments.map { if (it.id == id) it.copy(done = !it.done) else it })
    }

    fun removeCommit(id: String) = mutate {
        copy(commitments = commitments.filterNot { it.id == id })
    }

    fun snapshotToday() = mutate {
        val log = DayLog(
            date = date,
            energy = energy,
            wakeDone = wake.count { it.done },
            shutDone = shutdown.count { it.done },
            commitmentsDone = commitments.count { it.done },
            commitmentsTotal = commitments.size
        )
        val rest = history.filterNot { it.date == date }
        copy(history = (rest + log).takeLast(21))
    }

    private fun mutate(block: EmberState.() -> EmberState) {
        _ui.update { it.block() }
        viewModelScope.launch { store.persist(_ui.value) }
    }
}
