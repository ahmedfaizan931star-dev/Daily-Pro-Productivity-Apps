package com.dailyapps.momentumvault.data.repository

import android.content.Context
import com.dailyapps.momentumvault.data.local.AppDatabase
import com.dailyapps.momentumvault.data.model.EnergyLog
import com.dailyapps.momentumvault.data.model.FocusSession
import com.dailyapps.momentumvault.data.model.Habit
import com.dailyapps.momentumvault.data.model.HabitLog
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MomentumRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val habitDao = db.habitDao()
    private val focusDao = db.focusDao()
    private val energyDao = db.energyDao()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun today(): String = LocalDate.now().format(dateFormatter)

    fun startOfTodayMillis(): Long =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    // Habits
    fun getHabits(): Flow<List<Habit>> = habitDao.getAllHabits()
    fun getLogsForDate(date: String = today()): Flow<List<HabitLog>> = habitDao.getLogsForDate(date)

    suspend fun addHabit(title: String, iconName: String = "check") {
        habitDao.insertHabit(Habit(title = title, iconName = iconName))
    }

    suspend fun toggleHabit(habit: Habit, date: String = today()) {
        val alreadyDone = habit.lastCompletedDate == date
        if (alreadyDone) {
            habitDao.deleteLog(habit.id, date)
            val newStreak = (habit.currentStreak - 1).coerceAtLeast(0)
            habitDao.updateHabit(
                habit.copy(
                    currentStreak = newStreak,
                    lastCompletedDate = null
                )
            )
        } else {
            habitDao.insertLog(HabitLog(habitId = habit.id, date = date))
            val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
            val newStreak = if (habit.lastCompletedDate == yesterday) habit.currentStreak + 1 else 1
            val best = maxOf(habit.bestStreak, newStreak)
            habitDao.updateHabit(
                habit.copy(
                    currentStreak = newStreak,
                    bestStreak = best,
                    lastCompletedDate = date
                )
            )
        }
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.deleteHabit(id)
    }

    // Focus
    fun getFocusSessions(): Flow<List<FocusSession>> = focusDao.getAllSessions()
    fun getTodaySessions(): Flow<List<FocusSession>> =
        focusDao.getSessionsSince(startOfTodayMillis())

    suspend fun addFocusSession(minutes: Int, note: String = "") {
        focusDao.insertSession(FocusSession(durationMinutes = minutes, note = note))
    }

    // Energy
    fun getEnergyLogs(): Flow<List<EnergyLog>> = energyDao.getAllLogs()
    fun getTodayEnergy(): Flow<EnergyLog?> = energyDao.getLogForDate(today())

    suspend fun saveEnergyLog(energy: Int, mood: Int, journal: String) {
        val existing = energyDao.getLogForDate(today())
        // We insert/replace; simple approach
        energyDao.insertLog(
            EnergyLog(
                date = today(),
                energyLevel = energy,
                mood = mood,
                journal = journal
            )
        )
    }

    // Momentum score 0-100
    fun calculateMomentum(
        habits: List<Habit>,
        completedToday: Int,
        focusMinutesToday: Int,
        hasReflection: Boolean
    ): Int {
        val habitScore = if (habits.isEmpty()) 30 else {
            ((completedToday.toFloat() / habits.size) * 40).toInt()
        }
        val focusScore = (focusMinutesToday.coerceAtMost(120) / 120f * 40).toInt()
        val reflectScore = if (hasReflection) 20 else 0
        return (habitScore + focusScore + reflectScore).coerceIn(0, 100)
    }
}
