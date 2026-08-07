package com.dailyapps.pulseforge.data.repository

import android.content.Context
import com.dailyapps.pulseforge.data.local.AppDatabase
import com.dailyapps.pulseforge.data.model.FocusMode
import com.dailyapps.pulseforge.data.model.FocusSession
import com.dailyapps.pulseforge.data.model.Habit
import com.dailyapps.pulseforge.data.model.HabitCompletion
import com.dailyapps.pulseforge.data.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

class PulseRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val habitDao = db.habitDao()
    private val focusDao = db.focusDao()
    private val priorityDao = db.priorityDao()

    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()

    // Habits
    fun getHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    fun getTodayCompletions(): Flow<List<HabitCompletion>> =
        habitDao.getCompletionsForDay(todayEpochDay())

    suspend fun addHabit(title: String, colorHex: String = "#4F46E5") {
        habitDao.insertHabit(Habit(title = title, colorHex = colorHex))
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleHabitCompletion(habitId: Long, currentlyCompleted: Boolean) {
        val day = todayEpochDay()
        if (currentlyCompleted) {
            habitDao.removeCompletion(habitId, day)
        } else {
            habitDao.insertCompletion(HabitCompletion(habitId = habitId, dateEpochDay = day))
        }
    }

    fun getHabitStreak(habitId: Long): Flow<Int> {
        return habitDao.getCompletionsForHabit(habitId).map { completions ->
            if (completions.isEmpty()) return@map 0
            val days = completions.map { it.dateEpochDay }.toSet()
            var streak = 0
            var current = todayEpochDay()
            while (days.contains(current)) {
                streak++
                current--
            }
            if (streak == 0 && days.contains(todayEpochDay() - 1)) {
                current = todayEpochDay() - 1
                while (days.contains(current)) {
                    streak++
                    current--
                }
            }
            streak
        }
    }

    // Focus
    fun getRecentSessions(): Flow<List<FocusSession>> = focusDao.getRecentSessions()

    suspend fun logFocusSession(minutes: Int, mode: FocusMode, completed: Boolean = true) {
        focusDao.insertSession(
            FocusSession(
                durationMinutes = minutes,
                mode = mode.name,
                wasCompleted = completed
            )
        )
    }

    suspend fun getTodayFocusMinutes(): Int {
        val startOfDay = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return focusDao.getTotalFocusMinutesSince(startOfDay) ?: 0
    }

    // Priorities
    fun getTodayPriorities(): Flow<List<Priority>> =
        priorityDao.getPrioritiesForDay(todayEpochDay())

    suspend fun addPriority(title: String, orderIndex: Int) {
        priorityDao.insertPriority(
            Priority(
                title = title,
                dateEpochDay = todayEpochDay(),
                orderIndex = orderIndex
            )
        )
    }

    suspend fun togglePriority(priority: Priority) {
        priorityDao.updatePriority(priority.copy(isCompleted = !priority.isCompleted))
    }

    suspend fun deletePriority(priority: Priority) {
        priorityDao.deletePriority(priority)
    }

    data class DashboardSnapshot(
        val habits: List<Habit>,
        val completions: List<HabitCompletion>,
        val priorities: List<Priority>,
        val todayFocusMinutes: Int
    )

    fun getDashboardFlow(): Flow<DashboardSnapshot> {
        return combine(
            getHabits(),
            getTodayCompletions(),
            getTodayPriorities()
        ) { habits, completions, priorities ->
            Triple(habits, completions, priorities)
        }.map { (habits, completions, priorities) ->
            DashboardSnapshot(
                habits = habits,
                completions = completions,
                priorities = priorities,
                todayFocusMinutes = 0
            )
        }
    }
}
