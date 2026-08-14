package com.dailyapps.apexflow.data.repository

import android.content.Context
import com.dailyapps.apexflow.data.local.AppDatabase
import com.dailyapps.apexflow.data.model.DailyStats
import com.dailyapps.apexflow.data.model.FocusSession
import com.dailyapps.apexflow.data.model.Priority
import com.dailyapps.apexflow.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ApexRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val sessionDao = db.focusSessionDao()

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getActiveTasks(): Flow<List<Task>> = taskDao.getActiveTasks()
    fun getAllSessions(): Flow<List<FocusSession>> = sessionDao.getAllSessions()

    suspend fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
        taskDao.insert(Task(title = title, priority = priority))
    }

    suspend fun toggleTask(task: Task) {
        val updated = task.copy(
            isCompleted = !task.isCompleted,
            completedAt = if (!task.isCompleted) System.currentTimeMillis() else null
        )
        taskDao.update(updated)
    }

    suspend fun deleteTask(id: Long) {
        taskDao.delete(id)
    }

    suspend fun addFocusSession(durationMinutes: Int, note: String = "") {
        sessionDao.insert(FocusSession(durationMinutes = durationMinutes, note = note))
    }

    suspend fun getDailyStats(): DailyStats {
        val startOfDay = startOfDayMillis()
        val weekStart = startOfWeekMillis()
        return DailyStats(
            focusMinutesToday = sessionDao.getFocusMinutesToday(startOfDay),
            tasksCompletedToday = taskDao.countCompletedToday(startOfDay),
            totalTasks = taskDao.countTotal(),
            streakDays = 0, // simplified
            sessionsThisWeek = sessionDao.countSessionsThisWeek(weekStart)
        )
    }

    private fun startOfDayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun startOfWeekMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
