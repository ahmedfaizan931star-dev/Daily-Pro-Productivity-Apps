package com.dailyapps.orbitmind.data.repository

import android.content.Context
import com.dailyapps.orbitmind.data.local.AppDatabase
import com.dailyapps.orbitmind.data.model.DailyStats
import com.dailyapps.orbitmind.data.model.FocusSession
import com.dailyapps.orbitmind.data.model.Habit
import com.dailyapps.orbitmind.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class OrbitRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val habitDao = db.habitDao()
    private val focusDao = db.focusSessionDao()

    fun getActiveTasks(): Flow<List<Task>> = taskDao.getActiveTasks()
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getHabits(): Flow<List<Habit>> = habitDao.getAllHabits()
    fun getRecentSessions(): Flow<List<FocusSession>> = focusDao.getRecentSessions()

    suspend fun addTask(title: String, description: String = "", orbitLevel: Int = 2, minutes: Int = 25) {
        taskDao.insert(
            Task(
                title = title,
                description = description,
                orbitLevel = orbitLevel.coerceIn(1, 3),
                estimatedMinutes = minutes
            )
        )
    }

    suspend fun toggleTask(task: Task) {
        val updated = task.copy(
            isCompleted = !task.isCompleted,
            completedAt = if (!task.isCompleted) System.currentTimeMillis() else null
        )
        taskDao.update(updated)
    }

    suspend fun deleteTask(id: Long) = taskDao.delete(id)

    suspend fun addHabit(name: String, icon: String = "🌟") {
        habitDao.insert(Habit(name = name, icon = icon))
    }

    suspend fun completeHabit(habit: Habit) {
        val today = getTodayString()
        if (habit.lastCompletedDate == today) return
        val newStreak = if (isYesterday(habit.lastCompletedDate)) habit.streak + 1 else 1
        habitDao.update(
            habit.copy(
                streak = newStreak,
                lastCompletedDate = today
            )
        )
    }

    suspend fun deleteHabit(id: Long) = habitDao.delete(id)

    suspend fun logFocusSession(minutes: Int, taskId: Long? = null) {
        focusDao.insert(FocusSession(durationMinutes = minutes, taskId = taskId))
    }

    suspend fun getDailyStats(): DailyStats {
        val start = startOfDay()
        val completed = taskDao.countCompletedToday(start)
        val focusMins = focusDao.totalMinutesToday(start)
        return DailyStats(
            completedTasks = completed,
            totalFocusMinutes = focusMins,
            activeHabits = 0,
            longestStreak = 0
        )
    }

    private fun startOfDay(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getTodayString(): String {
        val cal = Calendar.getInstance()
        return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
    }

    private fun isYesterday(dateStr: String): Boolean {
        if (dateStr.isEmpty()) return false
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
        return dateStr == yesterday
    }
}
