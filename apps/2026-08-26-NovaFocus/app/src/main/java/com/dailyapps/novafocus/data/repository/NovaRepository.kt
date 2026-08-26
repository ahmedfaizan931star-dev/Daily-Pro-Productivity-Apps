package com.dailyapps.novafocus.data.repository

import android.content.Context
import com.dailyapps.novafocus.data.local.AppDatabase
import com.dailyapps.novafocus.data.model.FocusSession
import com.dailyapps.novafocus.data.model.Habit
import com.dailyapps.novafocus.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class NovaRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val habitDao = db.habitDao()
    private val taskDao = db.taskDao()
    private val focusDao = db.focusDao()

    val habits: Flow<List<Habit>> = habitDao.getAllHabits()
    val tasks: Flow<List<Task>> = taskDao.getAllTasks()
    val sessions: Flow<List<FocusSession>> = focusDao.getAllSessions()

    fun focusMinutesToday(): Flow<Int> {
        val startOfDay = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return focusDao.getFocusMinutesSince(startOfDay)
    }

    suspend fun addHabit(title: String, emoji: String = "\u2B50") {
        habitDao.insert(Habit(title = title, emoji = emoji))
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = LocalDate.now().toString()
        val alreadyDone = habit.lastCompletedDate == today
        val updated = if (alreadyDone) {
            habit.copy(
                streak = (habit.streak - 1).coerceAtLeast(0),
                lastCompletedDate = null
            )
        } else {
            habit.copy(
                streak = habit.streak + 1,
                lastCompletedDate = today
            )
        }
        habitDao.update(updated)
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.delete(id)
    }

    suspend fun addTask(title: String, priority: Int = 1) {
        taskDao.insert(Task(title = title, priority = priority))
    }

    suspend fun toggleTask(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(id: Long) {
        taskDao.delete(id)
    }

    suspend fun recordFocusSession(minutes: Int) {
        focusDao.insert(FocusSession(durationMinutes = minutes))
    }
}
