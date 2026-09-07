package com.dailyapps.keelstone.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dailyapps.keelstone.domain.Carve
import com.dailyapps.keelstone.domain.KeelStone
import com.dailyapps.keelstone.domain.WeekState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.UUID

private val Context.dataStore by preferencesDataStore("keelstone")

class KeelRepository(private val context: Context) {
    private val stonesKey = stringPreferencesKey("stones")
    private val carvesKey = stringPreferencesKey("carves")
    private val reviewKey = stringPreferencesKey("review")
    private val capKey = intPreferencesKey("capacity")
    private val weekKey = stringPreferencesKey("week")

    val state: Flow<WeekState> = context.dataStore.data.map { prefs ->
        val week = currentWeekLabel()
        val storedWeek = prefs[weekKey] ?: week
        val stones = decodeStones(prefs[stonesKey]).ifEmpty { seedStones() }
        val carves = decodeCarves(prefs[carvesKey])
        WeekState(
            weekLabel = if (storedWeek == week) storedWeek else week,
            capacityHours = prefs[capKey] ?: 20,
            stones = stones,
            carves = carves,
            reviewNote = prefs[reviewKey] ?: ""
        )
    }

    suspend fun save(state: WeekState) {
        context.dataStore.edit { prefs ->
            prefs[weekKey] = state.weekLabel
            prefs[capKey] = state.capacityHours
            prefs[reviewKey] = state.reviewNote
            prefs[stonesKey] = encodeStones(state.stones)
            prefs[carvesKey] = encodeCarves(state.carves)
        }
    }

    companion object {
        fun currentWeekLabel(): String {
            val monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            return "Week of " + monday.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        }

        fun seedStones(): List<KeelStone> = listOf(
            KeelStone(UUID.randomUUID().toString(), "Ship the client draft", "Unblocks Friday review", 8, 0f, false),
            KeelStone(UUID.randomUUID().toString(), "Deep study block", "Compound skill, not busywork", 6, 0f, false),
            KeelStone(UUID.randomUUID().toString(), "Health keel", "Sleep + lift so the week holds", 4, 0f, false)
        )

        private fun encodeStones(list: List<KeelStone>): String {
            val arr = JSONArray()
            list.forEach { s ->
                arr.put(JSONObject().apply {
                    put("id", s.id)
                    put("title", s.title)
                    put("why", s.why)
                    put("hoursTarget", s.hoursTarget)
                    put("hoursLogged", s.hoursLogged.toDouble())
                    put("done", s.done)
                })
            }
            return arr.toString()
        }

        private fun decodeStones(raw: String?): List<KeelStone> {
            if (raw.isNullOrBlank()) return emptyList()
            return try {
                val arr = JSONArray(raw)
                buildList {
                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        add(
                            KeelStone(
                                o.getString("id"),
                                o.getString("title"),
                                o.optString("why"),
                                o.optInt("hoursTarget", 4),
                                o.optDouble("hoursLogged", 0.0).toFloat(),
                                o.optBoolean("done")
                            )
                        )
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }

        private fun encodeCarves(list: List<Carve>): String {
            val arr = JSONArray()
            list.forEach { c ->
                arr.put(JSONObject().apply {
                    put("id", c.id)
                    put("stoneId", c.stoneId)
                    put("dayLabel", c.dayLabel)
                    put("minutes", c.minutes)
                    put("note", c.note)
                })
            }
            return arr.toString()
        }

        private fun decodeCarves(raw: String?): List<Carve> {
            if (raw.isNullOrBlank()) return emptyList()
            return try {
                val arr = JSONArray(raw)
                buildList {
                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        add(
                            Carve(
                                o.getString("id"),
                                o.getString("stoneId"),
                                o.getString("dayLabel"),
                                o.optInt("minutes", 45),
                                o.optString("note")
                            )
                        )
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
