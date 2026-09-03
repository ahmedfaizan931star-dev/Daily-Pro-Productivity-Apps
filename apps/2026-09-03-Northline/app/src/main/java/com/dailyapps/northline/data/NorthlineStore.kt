package com.dailyapps.northline.data

import android.content.Context
import com.dailyapps.northline.domain.Commitment
import com.dailyapps.northline.domain.DayPlan
import com.dailyapps.northline.domain.EnergyBand
import com.dailyapps.northline.domain.Friction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NorthlineStore(context: Context) {
    private val prefs = context.getSharedPreferences("northline", Context.MODE_PRIVATE)

    fun todayKey(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    fun loadToday(): DayPlan {
        val key = todayKey()
        val storedKey = prefs.getString("date", null)
        if (storedKey != key) {
            archiveIfNeeded(storedKey)
            val fresh = seed(key)
            saveToday(fresh)
            return fresh
        }
        return DayPlan(
            dateKey = key,
            northStar = prefs.getString("north", "") ?: "",
            commitments = decodeItems(prefs.getString("items", "") ?: ""),
            closed = prefs.getBoolean("closed", false),
            alignmentScore = prefs.getInt("score", 0)
        )
    }

    fun saveToday(plan: DayPlan) {
        prefs.edit()
            .putString("date", plan.dateKey)
            .putString("north", plan.northStar)
            .putString("items", encodeItems(plan.commitments))
            .putBoolean("closed", plan.closed)
            .putInt("score", plan.alignmentScore)
            .apply()
    }

    fun loadHistory(): List<DayPlan> {
        val raw = prefs.getString("history", "") ?: return emptyList()
        if (raw.isBlank()) return emptyList()
        return raw.split("||").mapNotNull { decodePlan(it) }
    }

    fun saveHistory(history: List<DayPlan>) {
        val raw = history.takeLast(21).joinToString("||") { encodePlan(it) }
        prefs.edit().putString("history", raw).apply()
    }

    private fun archiveIfNeeded(oldKey: String?) {
        if (oldKey.isNullOrBlank()) return
        val old = DayPlan(
            dateKey = oldKey,
            northStar = prefs.getString("north", "") ?: "",
            commitments = decodeItems(prefs.getString("items", "") ?: ""),
            closed = prefs.getBoolean("closed", false),
            alignmentScore = prefs.getInt("score", 0)
        )
        saveHistory(loadHistory() + old)
    }

    private fun seed(dateKey: String): DayPlan {
        val samples = listOf(
            Commitment("s1", "Draft the one-page brief", 5, EnergyBand.DEEP, Friction.NONE, false, System.currentTimeMillis()),
            Commitment("s2", "Reply to two blocked teammates", 3, EnergyBand.STEADY, Friction.WAITING, false, System.currentTimeMillis()),
            Commitment("s3", "Clear inbox to zero for 12 minutes", 2, EnergyBand.LOW, Friction.INTERRUPTIONS, false, System.currentTimeMillis())
        )
        return DayPlan(dateKey, "Ship a decision-ready brief", samples, false, 0)
    }

    private fun encodeItems(items: List<Commitment>): String =
        items.joinToString(";;") { c ->
            listOf(c.id, esc(c.title), c.leverage, c.energy.name, c.friction.name, c.done, c.createdAt)
                .joinToString("|")
        }

    private fun decodeItems(raw: String): List<Commitment> {
        if (raw.isBlank()) return emptyList()
        return raw.split(";;").mapNotNull { row ->
            val p = row.split("|")
            if (p.size < 7) return@mapNotNull null
            Commitment(
                id = p[0],
                title = unesc(p[1]),
                leverage = p[2].toIntOrNull() ?: 3,
                energy = runCatching { EnergyBand.valueOf(p[3]) }.getOrDefault(EnergyBand.STEADY),
                friction = runCatching { Friction.valueOf(p[4]) }.getOrDefault(Friction.NONE),
                done = p[5].toBoolean(),
                createdAt = p[6].toLongOrNull() ?: 0L
            )
        }
    }

    private fun encodePlan(plan: DayPlan): String =
        listOf(plan.dateKey, esc(plan.northStar), plan.closed, plan.alignmentScore, encodeItems(plan.commitments))
            .joinToString("::")

    private fun decodePlan(raw: String): DayPlan? {
        val p = raw.split("::")
        if (p.size < 5) return null
        return DayPlan(
            dateKey = p[0],
            northStar = unesc(p[1]),
            commitments = decodeItems(p[4]),
            closed = p[2].toBoolean(),
            alignmentScore = p[3].toIntOrNull() ?: 0
        )
    }

    private fun esc(s: String) = s.replace("|", "/").replace(";", ",").replace(":", "-")
    private fun unesc(s: String) = s
}
