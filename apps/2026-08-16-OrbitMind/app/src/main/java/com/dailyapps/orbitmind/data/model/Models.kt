package com.dailyapps.orbitmind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val orbitLevel: Int = 1, // 1 = core (highest priority), 2 = mid, 3 = outer
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val estimatedMinutes: Int = 25
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String = "🌟",
    val streak: Int = 0,
    val lastCompletedDate: String = "",
    val targetDays: Int = 7,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completed: Boolean = true,
    val startedAt: Long = System.currentTimeMillis(),
    val taskId: Long? = null
)

data class DailyStats(
    val completedTasks: Int = 0,
    val totalFocusMinutes: Int = 0,
    val activeHabits: Int = 0,
    val longestStreak: Int = 0
)
