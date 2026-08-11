package com.dailyapps.cadencecore.data.repository

import android.content.Context
import com.dailyapps.cadencecore.data.local.AppDatabase
import com.dailyapps.cadencecore.data.model.FocusSession
import com.dailyapps.cadencecore.data.model.Habit
import com.dailyapps.cadencecore.data.model.HabitLog
import com.dailyapps.cadencecore.data.model.HabitWithProgress
import com.dailyapps.cadencecore.data.model.Reflection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CadenceRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val habitDao = db.habitDao()
    private val focusDao = db.focusDao()
    private val reflectionDao = db.reflectionDao()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun todayKey(): String = LocalDate.now().format(dateFormatter)

    fun getHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    fun getHabitsWithProgress(): Flow<List<HabitWithProgress>> {
        val today = todayKey()
        return combine(
            habitDao.getAllHabits(),
            habitDao.getLogsForDay(today)
        ) { habits, logs ->
            habits.map { habit ->
                val todayCount = logs.find { it.habitId == habit.id }?.count ?: 0
                // Streak calculated simply from today's log presence for UI; full streak is best-effort
                val streak = if (todayCount > 0) 1 else 0
                HabitWithProgress(habit, todayCount, streak)
            }
        }
    }

    suspend fun refreshStreaks(habits: List<Habit>): Map<Long, Int> {
        return habits.associate { habit ->
            habit.id to calculateStreak(habit.id)
        }
    }

    private suspend fun calculateStreak(habitId: Long): Int {
        val logs = habitDao.getLogsForHabit(habitId)
        if (logs.isEmpty()) return 0
        val dates = logs.filter { it.count > 0 }.map { it.dateKey }.toSet()
        var streak = 0
        var day = LocalDate.now()
        // Count consecutive days ending today or yesterday
        if (!dates.contains(day.format(dateFormatter))) {
            day = day.minusDays(1)
        }
        while (dates.contains(day.format(dateFormatter))) {
            streak++
            day = day.minusDays(1)
        }
        return streak
    }

    suspend fun addHabit(title: String, iconKey: String = "check") {
        habitDao.insertHabit(Habit(title = title, iconKey = iconKey))
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.deleteHabit(id)
    }

    suspend fun logHabit(habitId: Long) {
        val today = todayKey()
        val existing = habitDao.getLogForDay(habitId, today)
        if (existing == null) {
            habitDao.insertLog(HabitLog(habitId = habitId, dateKey = today, count = 1))
        } else {
            val newCount = if (existing.count >= 1) 0 else 1
            habitDao.insertLog(existing.copy(count = newCount))
        }
    }

    fun getFocusMinutesToday(): Flow<Int> = focusDao.getTotalMinutesForDay(todayKey())

    fun getSessionsToday(): Flow<List<FocusSession>> = focusDao.getSessionsForDay(todayKey())

    suspend fun addFocusSession(minutes: Int, note: String = "") {
        focusDao.insertSession(
            FocusSession(
                durationMinutes = minutes,
                completed = true,
                note = note,
                dateKey = todayKey()
            )
        )
    }

    fun getReflectionToday(): Flow<Reflection?> = reflectionDao.getReflectionForDay(todayKey())

    fun getAllReflections(): Flow<List<Reflection>> = reflectionDao.getAllReflections()

    suspend fun saveReflection(mood: Int, energy: Int, content: String) {
        val today = todayKey()
        val existing = reflectionDao.getReflectionForDay(today).first()
        if (existing == null) {
            reflectionDao.insertReflection(
                Reflection(dateKey = today, mood = mood, energy = energy, content = content)
            )
        } else {
            reflectionDao.updateReflection(
                existing.copy(mood = mood, energy = energy, content = content, timestamp = System.currentTimeMillis())
            )
        }
    }

    suspend fun seedIfEmpty() {
        val habits = habitDao.getAllHabits().first()
        if (habits.isEmpty()) {
            habitDao.insertHabit(Habit(title = "Morning stretch", iconKey = "fitness"))
            habitDao.insertHabit(Habit(title = "Deep work block", iconKey = "work"))
            habitDao.insertHabit(Habit(title = "Hydrate", iconKey = "water"))
            habitDao.insertHabit(Habit(title = "Evening review", iconKey = "review"))
        }
    }
}
