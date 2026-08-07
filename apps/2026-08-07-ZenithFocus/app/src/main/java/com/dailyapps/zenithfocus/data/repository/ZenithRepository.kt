package com.dailyapps.zenithfocus.data.repository

import android.content.Context
import com.dailyapps.zenithfocus.data.local.AppDatabase
import com.dailyapps.zenithfocus.data.model.EnergyLevel
import com.dailyapps.zenithfocus.data.model.FocusSession
import com.dailyapps.zenithfocus.data.model.MatrixQuadrant
import com.dailyapps.zenithfocus.data.model.Reflection
import com.dailyapps.zenithfocus.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ZenithRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val focusDao = db.focusSessionDao()
    private val reflectionDao = db.reflectionDao()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun todayKey(): String = LocalDate.now().format(dateFormatter)

    // Tasks
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getOpenTasks(): Flow<List<Task>> = taskDao.getOpenTasks()
    fun completedTodayCount(): Flow<Int> = taskDao.completedTodayCount()

    suspend fun addTask(
        title: String,
        notes: String = "",
        quadrant: MatrixQuadrant = MatrixQuadrant.DO_FIRST,
        energy: EnergyLevel = EnergyLevel.MEDIUM
    ) {
        taskDao.insert(
            Task(
                title = title.trim(),
                notes = notes.trim(),
                quadrant = quadrant,
                energyRequired = energy
            )
        )
    }

    suspend fun toggleTaskCompleted(task: Task) {
        val updated = if (task.isCompleted) {
            task.copy(isCompleted = false, completedAt = null)
        } else {
            task.copy(isCompleted = true, completedAt = System.currentTimeMillis())
        }
        taskDao.update(updated)
    }

    suspend fun deleteTask(id: Long) {
        taskDao.delete(id)
    }

    // Focus
    fun focusMinutesToday(): Flow<Int> = focusDao.focusMinutesToday()
    fun recentSessions(): Flow<List<FocusSession>> = focusDao.getRecentSessions()

    suspend fun logFocusSession(minutes: Int, mode: String, taskId: Long? = null) {
        focusDao.insert(
            FocusSession(
                durationMinutes = minutes,
                mode = mode,
                taskId = taskId
            )
        )
    }

    // Reflections
    fun getTodayReflection(): Flow<Reflection?> = reflectionDao.getForDate(todayKey())
    fun getRecentReflections(): Flow<List<Reflection>> = reflectionDao.getRecent()

    suspend fun saveReflection(reflection: Reflection) {
        reflectionDao.upsert(reflection.copy(dateKey = todayKey(), updatedAt = System.currentTimeMillis()))
    }
}
