package com.dailyapps.solsticeflow.data.repository

import com.dailyapps.solsticeflow.data.local.AppDatabase
import com.dailyapps.solsticeflow.data.model.DailyReview
import com.dailyapps.solsticeflow.data.model.EnergyLog
import com.dailyapps.solsticeflow.data.model.FocusSession
import com.dailyapps.solsticeflow.data.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class SolsticeRepository(private val db: AppDatabase) {

    private fun startOfToday(): Long =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    fun getTodayEnergyLogs(): Flow<List<EnergyLog>> =
        db.energyDao().getTodayLogs(startOfToday())

    fun getRecentEnergyLogs(): Flow<List<EnergyLog>> =
        db.energyDao().getRecentLogs()

    suspend fun addEnergyLog(level: Int, note: String = "") {
        db.energyDao().insert(EnergyLog(level = level.coerceIn(1, 5), note = note))
    }

    fun getHabits(): Flow<List<Habit>> = db.habitDao().getAll()

    suspend fun addHabit(title: String, icon: String = "☀️") {
        db.habitDao().insert(Habit(title = title, icon = icon))
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = LocalDate.now().toString()
        val updated = if (habit.lastCompletedDate == today) {
            habit.copy(streak = (habit.streak - 1).coerceAtLeast(0), lastCompletedDate = "")
        } else {
            habit.copy(streak = habit.streak + 1, lastCompletedDate = today)
        }
        db.habitDao().update(updated)
    }

    suspend fun deleteHabit(id: Long) = db.habitDao().delete(id)

    fun getTodayFocusMinutes(): Flow<Int> =
        db.focusDao().getTodayMinutes(startOfToday())

    fun getRecentSessions(): Flow<List<FocusSession>> = db.focusDao().getRecent()

    suspend fun saveFocusSession(minutes: Int, energy: Int) {
        db.focusDao().insert(FocusSession(durationMinutes = minutes, energyAtStart = energy))
    }

    suspend fun getTodayReview(): DailyReview? =
        db.reviewDao().getByDate(LocalDate.now().toString())

    suspend fun saveReview(wins: String, challenges: String, energyAvg: Float, focusMin: Int) {
        db.reviewDao().upsert(
            DailyReview(
                date = LocalDate.now().toString(),
                wins = wins,
                challenges = challenges,
                energyAverage = energyAvg,
                focusMinutes = focusMin
            )
        )
    }

    fun getRecentReviews(): Flow<List<DailyReview>> = db.reviewDao().getRecent()
}
