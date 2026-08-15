package com.dailyapps.aetherforge.data.repository

import android.content.Context
import com.dailyapps.aetherforge.data.local.AppDatabase
import com.dailyapps.aetherforge.data.model.DailyStats
import com.dailyapps.aetherforge.data.model.EnergyLevel
import com.dailyapps.aetherforge.data.model.FocusSessionEntity
import com.dailyapps.aetherforge.data.model.Priority
import com.dailyapps.aetherforge.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AetherRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val focusDao = db.focusDao()

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    fun getActiveTasks(): Flow<List<TaskEntity>> = taskDao.getActiveTasks()
    fun getRecentSessions(): Flow<List<FocusSessionEntity>> = focusDao.getRecentSessions()

    suspend fun addTask(title: String, description: String = "", priority: Priority = Priority.MEDIUM, energy: EnergyLevel = EnergyLevel.MEDIUM) {
        taskDao.insert(TaskEntity(title = title, description = description, priority = priority, energy = energy))
    }

    suspend fun toggleComplete(task: TaskEntity) {
        val updated = task.copy(
            isCompleted = !task.isCompleted,
            completedAt = if (!task.isCompleted) System.currentTimeMillis() else null
        )
        taskDao.update(updated)
    }

    suspend fun deleteTask(id: Long) = taskDao.delete(id)

    suspend fun logFocusSession(taskId: Long?, minutes: Int) {
        focusDao.insert(FocusSessionEntity(taskId = taskId, durationMinutes = minutes))
    }

    suspend fun getDailyStats(): DailyStats {
        val start = startOfToday()
        val completed = taskDao.countCompletedToday(start)
        val total = taskDao.countCreatedToday(start).coerceAtLeast(completed)
        val focus = focusDao.totalMinutesToday(start)
        val score = ((completed * 20) + (focus.coerceAtMost(120) / 2)).coerceIn(0, 100)
        return DailyStats(completedTasks = completed, totalTasks = total, focusMinutes = focus, streak = if (completed > 0) 1 else 0, energyScore = score)
    }

    private fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
