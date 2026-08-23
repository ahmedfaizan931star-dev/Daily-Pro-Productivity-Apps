package com.dailyapps.nexusflow.data.repository

import com.dailyapps.nexusflow.data.local.AppDatabase
import com.dailyapps.nexusflow.data.model.FocusSession
import com.dailyapps.nexusflow.data.model.Habit
import com.dailyapps.nexusflow.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NexusRepository(private val db: AppDatabase) {
    private val habitDao = db.habitDao()
    private val taskDao = db.taskDao()
    private val focusDao = db.focusDao()

    val habits: Flow<List<Habit>> = habitDao.getAll()
    val tasks: Flow<List<Task>> = taskDao.getAll()
    val focusSessions: Flow<List<FocusSession>> = focusDao.getAll()

    suspend fun addHabit(title: String) {
        habitDao.insert(Habit(title = title))
    }

    suspend fun toggleHabit(habit: Habit) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val alreadyDoneToday = habit.lastCompletedDate == today
        val newStreak = if (alreadyDoneToday) {
            maxOf(0, habit.streak - 1)
        } else {
            habit.streak + 1
        }
        val newDate = if (alreadyDoneToday) null else today
        habitDao.update(habit.copy(streak = newStreak, lastCompletedDate = newDate))
    }

    suspend fun deleteHabit(id: Long) {
        habitDao.delete(id)
    }

    suspend fun addTask(title: String, priority: Int) {
        taskDao.insert(Task(title = title, priority = priority))
    }

    suspend fun toggleTask(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(id: Long) {
        taskDao.delete(id)
    }

    suspend fun logFocusSession(minutes: Int) {
        focusDao.insert(FocusSession(durationMinutes = minutes))
    }

    suspend fun todayFocusMinutes(): Int {
        val startOfDay = LocalDate.now().atStartOfDay()
            .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        return focusDao.totalMinutesSince(startOfDay)
    }
}
