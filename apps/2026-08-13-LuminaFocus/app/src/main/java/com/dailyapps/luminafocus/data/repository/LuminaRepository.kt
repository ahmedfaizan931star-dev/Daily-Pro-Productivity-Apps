package com.dailyapps.luminafocus.data.repository

import android.content.Context
import com.dailyapps.luminafocus.data.local.AppDatabase
import com.dailyapps.luminafocus.data.model.EnergyLog
import com.dailyapps.luminafocus.data.model.FocusSession
import com.dailyapps.luminafocus.data.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class LuminaRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val sessionDao = db.focusSessionDao()
    private val habitDao = db.habitDao()
    private val energyDao = db.energyLogDao()

    private fun startOfToday(): Long {
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getAllSessions(): Flow<List<FocusSession>> = sessionDao.getAll()
    fun getTodaySessions(): Flow<List<FocusSession>> = sessionDao.getToday(startOfToday())
    fun getTodayFocusMinutes(): Flow<Int> = sessionDao.getTodayMinutes(startOfToday())

    suspend fun addSession(durationMinutes: Int, mode: String = "pomodoro") {
        sessionDao.insert(FocusSession(durationMinutes = durationMinutes, mode = mode))
    }

    fun getHabits(): Flow<List<Habit>> = habitDao.getAll()

    suspend fun addHabit(title: String, icon: String = "⭐") {
        habitDao.insert(Habit(title = title, icon = icon))
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = LocalDate.now().toString()
        val alreadyDone = habit.lastCompletedDate == today
        val updated = if (alreadyDone) {
            habit.copy(
                streak = (habit.streak - 1).coerceAtLeast(0),
                lastCompletedDate = null
            )
        } else {
            habit.copy(
                streak = habit.streak + 1,
                lastCompletedDate = today
            )
        }
        habitDao.update(updated)
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.delete(id)
    }

    fun getEnergyLogs(): Flow<List<EnergyLog>> = energyDao.getAll()
    fun getTodayEnergyLogs(): Flow<List<EnergyLog>> = energyDao.getToday(startOfToday())

    suspend fun logEnergy(energy: Int, mood: Int, note: String = "") {
        energyDao.insert(EnergyLog(energyLevel = energy, mood = mood, note = note))
    }
}
