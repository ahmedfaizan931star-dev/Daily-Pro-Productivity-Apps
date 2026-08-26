package com.dailyapps.novafocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val emoji: String = "\u2B50",
    val streak: Int = 0,
    val lastCompletedDate: String? = null, // yyyy-MM-dd
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val priority: Int = 1, // 0=low, 1=medium, 2=high
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis()
)

data class DailyStats(
    val focusMinutesToday: Int = 0,
    val habitsCompletedToday: Int = 0,
    val totalHabits: Int = 0,
    val tasksCompletedToday: Int = 0,
    val totalTasks: Int = 0
)
