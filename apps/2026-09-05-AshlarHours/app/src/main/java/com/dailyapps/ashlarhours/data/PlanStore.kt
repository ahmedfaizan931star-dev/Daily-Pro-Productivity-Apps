package com.dailyapps.ashlarhours.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dailyapps.ashlarhours.domain.HourStone
import com.dailyapps.ashlarhours.domain.StoneKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ashlar_hours")

class PlanStore(private val context: Context) {

    private val payloadKey = stringPreferencesKey("payload")
    private val deepTargetKey = intPreferencesKey("deep_target")

    val payload: Flow<String> = context.dataStore.data.map { it[payloadKey] ?: "" }
    val deepTarget: Flow<Int> = context.dataStore.data.map { it[deepTargetKey] ?: 4 }

    suspend fun save(payload: String) {
        context.dataStore.edit { it[payloadKey] = payload }
    }

    suspend fun setDeepTarget(hours: Int) {
        context.dataStore.edit { it[deepTargetKey] = hours.coerceIn(1, 10) }
    }

    companion object {
        fun encode(stones: List<HourStone>): String =
            stones.joinToString("|") { s ->
                listOf(s.id, s.hour.toString(), s.kind.name, s.title.replace("|", "/").replace(";", ","), if (s.done) "1" else "0")
                    .joinToString(";")
            }

        fun decode(raw: String): List<HourStone> {
            if (raw.isBlank()) return emptyList()
            return raw.split("|").mapNotNull { row ->
                val p = row.split(";")
                if (p.size < 5) return@mapNotNull null
                val kind = runCatching { StoneKind.valueOf(p[2]) }.getOrDefault(StoneKind.EMPTY)
                HourStone(
                    id = p[0],
                    hour = p[1].toIntOrNull() ?: 9,
                    kind = kind,
                    title = p[3],
                    done = p[4] == "1"
                )
            }
        }
    }
}
