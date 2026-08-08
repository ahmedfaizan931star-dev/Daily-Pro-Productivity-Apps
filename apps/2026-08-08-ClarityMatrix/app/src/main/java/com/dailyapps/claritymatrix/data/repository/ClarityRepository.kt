package com.dailyapps.claritymatrix.data.repository

import android.content.Context
import com.dailyapps.claritymatrix.data.local.AppDatabase
import com.dailyapps.claritymatrix.data.model.Quadrant
import com.dailyapps.claritymatrix.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ClarityRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).taskDao()

    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()
    fun getActiveTasks(): Flow<List<Task>> = dao.getActiveTasks()
    fun getTasksByQuadrant(quadrant: Quadrant): Flow<List<Task>> = dao.getTasksByQuadrant(quadrant)

    suspend fun getTask(id: Long): Task? = dao.getTaskById(id)

    suspend fun addTask(title: String, notes: String, quadrant: Quadrant, estimatedMinutes: Int = 25): Long {
        val task = Task(
            title = title.trim(),
            notes = notes.trim(),
            quadrant = quadrant,
            estimatedMinutes = estimatedMinutes
        )
        return dao.insertTask(task)
    }

    suspend fun updateTask(task: Task) = dao.updateTask(task)

    suspend fun toggleComplete(task: Task) {
        val updated = if (task.isCompleted) {
            task.copy(isCompleted = false, completedAt = null)
        } else {
            task.copy(isCompleted = true, completedAt = System.currentTimeMillis())
        }
        dao.updateTask(updated)
    }

    suspend fun deleteTask(id: Long) = dao.deleteTask(id)

    suspend fun getCompletedToday(): Int {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getCompletedTodayCount(cal.timeInMillis)
    }

    suspend fun getActiveCount(): Int = dao.getActiveCount()
}
