package com.dailyapps.apexflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

enum class Priority {
    HIGH, MEDIUM, LOW
}

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val note: String = ""
)

data class DailyStats(
    val focusMinutesToday: Int = 0,
    val tasksCompletedToday: Int = 0,
    val totalTasks: Int = 0,
    val streakDays: Int = 0,
    val sessionsThisWeek: Int = 0
)
