package com.dailyapps.vortexvault.data.repository

import com.dailyapps.vortexvault.data.local.AppDatabase
import com.dailyapps.vortexvault.data.model.FocusSession
import com.dailyapps.vortexvault.data.model.Habit
import com.dailyapps.vortexvault.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class VortexRepository(private val db: AppDatabase) {

    val habits: Flow<List<Habit>> = db.habitDao().getAll()
    val tasks: Flow<List<Task>> = db.taskDao().getAll()
    val recentSessions: Flow<List<FocusSession>> = db.focusSessionDao().getRecent()

    fun todayFocusMinutes(): Flow<Int> {
        val startOfDay = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return db.focusSessionDao().getTodayMinutes(startOfDay)
    }

    suspend fun addHabit(title: String) {
        db.habitDao().insert(Habit(title = title.trim()))
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = LocalDate.now().toString()
        val updated = if (habit.lastCompletedDate == today) {
            habit.copy(streak = (habit.streak - 1).coerceAtLeast(0), lastCompletedDate = null)
        } else {
            val newStreak = if (habit.lastCompletedDate == LocalDate.now().minusDays(1).toString()) {
                habit.streak + 1
            } else {
                1
            }
            habit.copy(streak = newStreak, lastCompletedDate = today)
        }
        db.habitDao().update(updated)
    }

    suspend fun deleteHabit(id: Long) {
        db.habitDao().delete(id)
    }

    suspend fun addTask(title: String, priority: Int) {
        db.taskDao().insert(Task(title = title.trim(), priority = priority))
    }

    suspend fun toggleTask(task: Task) {
        db.taskDao().update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(id: Long) {
        db.taskDao().delete(id)
    }

    suspend fun saveFocusSession(minutes: Int) {
        db.focusSessionDao().insert(FocusSession(durationMinutes = minutes))
    }
}
