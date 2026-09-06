package com.dailyapps.emberwake.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dailyapps.emberwake.domain.Commitment
import com.dailyapps.emberwake.domain.DayLog
import com.dailyapps.emberwake.domain.EmberState
import com.dailyapps.emberwake.domain.RitualStep
import com.dailyapps.emberwake.domain.defaultShutdown
import com.dailyapps.emberwake.domain.defaultWake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore by preferencesDataStore("emberwake")

class EmberStore(private val context: Context) {
    private val dateKey = stringPreferencesKey("date")
    private val energyKey = intPreferencesKey("energy")
    private val wakeKey = stringPreferencesKey("wake")
    private val shutKey = stringPreferencesKey("shut")
    private val commitKey = stringPreferencesKey("commits")
    private val historyKey = stringPreferencesKey("history")

    val state: Flow<EmberState> = context.dataStore.data.map { prefs ->
        val today = LocalDate.now().toString()
        val storedDate = prefs[dateKey] ?: today
        val sameDay = storedDate == today
        EmberState(
            date = today,
            energy = if (sameDay) prefs[energyKey] ?: 6 else 6,
            wake = if (sameDay) decodeSteps(prefs[wakeKey], defaultWake()) else defaultWake(),
            shutdown = if (sameDay) decodeSteps(prefs[shutKey], defaultShutdown()) else defaultShutdown(),
            commitments = if (sameDay) decodeCommits(prefs[commitKey]) else emptyList(),
            history = decodeHistory(prefs[historyKey])
        )
    }

    suspend fun persist(state: EmberState) {
        context.dataStore.edit { prefs ->
            prefs[dateKey] = state.date
            prefs[energyKey] = state.energy
            prefs[wakeKey] = encodeSteps(state.wake)
            prefs[shutKey] = encodeSteps(state.shutdown)
            prefs[commitKey] = encodeCommits(state.commitments)
            prefs[historyKey] = encodeHistory(state.history)
        }
    }

    private fun encodeSteps(list: List<RitualStep>): String =
        list.joinToString("||") { "${it.id}::${it.title}::${if (it.done) 1 else 0}" }

    private fun decodeSteps(raw: String?, fallback: List<RitualStep>): List<RitualStep> {
        if (raw.isNullOrBlank()) return fallback
        return raw.split("||").mapNotNull { part ->
            val bits = part.split("::")
            if (bits.size < 3) null
            else RitualStep(bits[0], bits[1], bits[2] == "1")
        }.ifEmpty { fallback }
    }

    private fun encodeCommits(list: List<Commitment>): String =
        list.joinToString("||") { "${it.id}::${it.title}::${if (it.done) 1 else 0}::${it.energyCost}" }

    private fun decodeCommits(raw: String?): List<Commitment> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split("||").mapNotNull { part ->
            val bits = part.split("::")
            if (bits.size < 4) null
            else Commitment(bits[0], bits[1], bits[2] == "1", bits[3].toIntOrNull() ?: 2)
        }
    }

    private fun encodeHistory(list: List<DayLog>): String =
        list.takeLast(21).joinToString("||") {
            "${it.date}::${it.energy}::${it.wakeDone}::${it.shutDone}::${it.commitmentsDone}::${it.commitmentsTotal}"
        }

    private fun decodeHistory(raw: String?): List<DayLog> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split("||").mapNotNull { part ->
            val bits = part.split("::")
            if (bits.size < 6) null
            else DayLog(
                bits[0],
                bits[1].toIntOrNull() ?: 0,
                bits[2].toIntOrNull() ?: 0,
                bits[3].toIntOrNull() ?: 0,
                bits[4].toIntOrNull() ?: 0,
                bits[5].toIntOrNull() ?: 0
            )
        }
    }
}
