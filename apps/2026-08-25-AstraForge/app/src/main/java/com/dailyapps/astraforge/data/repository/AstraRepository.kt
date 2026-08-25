package com.dailyapps.astraforge.data.repository

import android.content.Context
import com.dailyapps.astraforge.data.local.AppDatabase
import com.dailyapps.astraforge.data.model.FocusSession
import com.dailyapps.astraforge.data.model.Habit
import com.dailyapps.astraforge.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AstraRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val habitDao = db.habitDao()
    private val taskDao = db.taskDao()
    private val focusDao = db.focusSessionDao()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun getHabits(): Flow<List<Habit>> = habitDao.getAll()
    fun getTasks(): Flow<List<Task>> = taskDao.getAll()
    fun getFocusSessions(): Flow<List<FocusSession>> = focusDao.getAll()

    fun getTodayFocusMinutes(): Flow<Int> {
        val startOfDay = LocalDate.now().atStartOfDay()
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        return focusDao.getTodayMinutes(startOfDay)
    }

    suspend fun addHabit(title: String) {
        habitDao.insert(Habit(title = title.trim()))
    }

    suspend fun completeHabit(habit: Habit) {
        val today = LocalDate.now().format(dateFormatter)
        if (habit.lastCompletedDate == today) return
        val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
        val newStreak = if (habit.lastCompletedDate == yesterday) habit.streak + 1 else 1
        habitDao.update(habit.copy(streak = newStreak, lastCompletedDate = today))
    }

    suspend fun deleteHabit(id: Long) = habitDao.delete(id)

    suspend fun addTask(title: String, priority: Int = 2) {
        taskDao.insert(Task(title = title.trim(), priority = priority))
    }

    suspend fun toggleTask(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(id: Long) = taskDao.delete(id)

    suspend fun logFocusSession(minutes: Int) {
        focusDao.insert(FocusSession(durationMinutes = minutes))
    }
}
