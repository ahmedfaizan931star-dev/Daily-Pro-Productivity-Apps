package com.dailyapps.cascadeflow.data.repository

import android.content.Context
import com.dailyapps.cascadeflow.data.db.AppDatabase
import com.dailyapps.cascadeflow.data.model.CascadeStats
import com.dailyapps.cascadeflow.data.model.FocusSession
import com.dailyapps.cascadeflow.data.model.Habit
import com.dailyapps.cascadeflow.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CascadeRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val taskDao = db.taskDao()
    private val habitDao = db.habitDao()
    private val focusDao = db.focusSessionDao()

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getActiveTasks(): Flow<List<Task>> = taskDao.getActiveTasks()
    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()
    fun getRecentFocusSessions(): Flow<List<FocusSession>> = focusDao.getRecentSessions()

    suspend fun addTask(task: Task) = taskDao.insert(task)
    suspend fun updateTask(task: Task) = taskDao.update(task)
    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    suspend fun addHabit(habit: Habit) = habitDao.insert(habit)
    suspend fun updateHabit(habit: Habit) = habitDao.update(habit)
    suspend fun deleteHabit(habit: Habit) = habitDao.delete(habit)

    suspend fun addFocusSession(session: FocusSession) = focusDao.insert(session)

    private fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    suspend fun getStats(): CascadeStats {
        val start = startOfToday()
        val completed = taskDao.countCompletedToday(start)
        val total = taskDao.countTotal()
        val focusMin = focusDao.sumMinutesToday(start)
        val habitsDone = habitDao.countCompletedToday()
        // Simple cascade score: weighted mix of completion rate, focus, habits
        val taskScore = if (total > 0) (completed * 40 / total) else 0
        val focusScore = (focusMin.coerceAtMost(120) * 30 / 120)
        val habitScore = (habitsDone.coerceAtMost(5) * 30 / 5)
        val score = (taskScore + focusScore + habitScore).coerceIn(0, 100)
        return CascadeStats(
            tasksCompletedToday = completed,
            totalTasks = total,
            focusMinutesToday = focusMin,
            activeHabits = habitsDone,
            cascadeScore = score
        )
    }
}
