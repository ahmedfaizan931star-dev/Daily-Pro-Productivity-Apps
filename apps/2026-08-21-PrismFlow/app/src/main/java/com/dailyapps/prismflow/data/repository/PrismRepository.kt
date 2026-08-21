package com.dailyapps.prismflow.data.repository

import android.content.Context
import com.dailyapps.prismflow.data.local.AppDatabase
import com.dailyapps.prismflow.data.model.EnergyLog
import com.dailyapps.prismflow.data.model.Habit
import com.dailyapps.prismflow.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PrismRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val habitDao = db.habitDao()
    private val energyDao = db.energyDao()

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()
    val habits: Flow<List<Habit>> = habitDao.getAllHabits()
    val energyLogs: Flow<List<EnergyLog>> = energyDao.getRecentLogs()

    suspend fun addTask(title: String, notes: String = "", quadrant: com.dailyapps.prismflow.data.model.PriorityQuadrant) {
        taskDao.insert(Task(title = title, notes = notes, quadrant = quadrant))
    }

    suspend fun toggleTask(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(id: Long) {
        taskDao.delete(id)
    }

    suspend fun addHabit(name: String) {
        habitDao.insert(Habit(name = name))
    }

    suspend fun completeHabit(habit: Habit) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val newStreak = if (habit.lastCompletedDate == today) habit.streak
        else if (habit.lastCompletedDate == LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
            habit.streak + 1
        else 1
        habitDao.update(habit.copy(streak = newStreak, lastCompletedDate = today))
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.delete(id)
    }

    suspend fun logEnergy(level: Int, note: String = "") {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        energyDao.insert(EnergyLog(date = today, level = level, note = note))
    }
}
