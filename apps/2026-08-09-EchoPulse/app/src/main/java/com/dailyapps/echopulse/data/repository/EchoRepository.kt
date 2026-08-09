package com.dailyapps.echopulse.data.repository

import com.dailyapps.echopulse.data.local.AppDatabase
import com.dailyapps.echopulse.data.model.DailyPulse
import com.dailyapps.echopulse.data.model.FocusSession
import com.dailyapps.echopulse.data.model.Habit
import com.dailyapps.echopulse.data.model.Reflection
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EchoRepository(private val db: AppDatabase) {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun today(): String = LocalDate.now().format(dateFormatter)

    fun getHabits(): Flow<List<Habit>> = db.habitDao().getAllHabits()

    suspend fun addHabit(title: String, colorHex: String = "#0EA5E9") {
        db.habitDao().insert(
            Habit(title = title, colorHex = colorHex)
        )
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = today()
        val newCompleted = !habit.completedToday
        var newStreak = habit.streak
        var best = habit.bestStreak

        if (newCompleted) {
            if (habit.lastCompletedDate != today) {
                // simple streak logic: if last was yesterday keep, else reset
                val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
                newStreak = if (habit.lastCompletedDate == yesterday || habit.lastCompletedDate.isEmpty()) {
                    habit.streak + 1
                } else {
                    1
                }
                if (newStreak > best) best = newStreak
            }
        } else {
            // un-complete today
            newStreak = (habit.streak - 1).coerceAtLeast(0)
        }

        db.habitDao().update(
            habit.copy(
                completedToday = newCompleted,
                streak = newStreak,
                bestStreak = best,
                lastCompletedDate = if (newCompleted) today else habit.lastCompletedDate
            )
        )
    }

    suspend fun deleteHabit(id: Long) {
        db.habitDao().delete(id)
    }

    fun getFocusSessions(): Flow<List<FocusSession>> = db.focusSessionDao().getAllSessions()

    fun getTodayFocusSessions(startOfDay: Long): Flow<List<FocusSession>> =
        db.focusSessionDao().getTodaySessions(startOfDay)

    suspend fun addFocusSession(minutes: Int, note: String = "") {
        db.focusSessionDao().insert(
            FocusSession(durationMinutes = minutes, note = note)
        )
    }

    fun getReflections(): Flow<List<Reflection>> = db.reflectionDao().getAllReflections()

    fun getTodayReflection(): Flow<Reflection?> =
        db.reflectionDao().getReflectionForDate(today())

    suspend fun saveReflection(content: String, energyLevel: Int, mood: String) {
        val existing = null // simplified insert; Room replace on conflict not used for id
        db.reflectionDao().insert(
            Reflection(
                content = content,
                energyLevel = energyLevel,
                mood = mood,
                date = today()
            )
        )
    }

    fun getPulse(date: String = today()): Flow<DailyPulse?> =
        db.dailyPulseDao().getPulse(date)

    suspend fun upsertPulse(pulse: DailyPulse) {
        db.dailyPulseDao().upsert(pulse)
    }
}
