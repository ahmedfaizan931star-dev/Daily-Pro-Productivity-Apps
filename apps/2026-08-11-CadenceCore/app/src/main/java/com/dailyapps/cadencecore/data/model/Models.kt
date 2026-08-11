package com.dailyapps.cadencecore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val iconKey: String = "check",
    val targetPerDay: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val dateKey: String, // yyyy-MM-dd
    val count: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completed: Boolean = true,
    val note: String = "",
    val dateKey: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val mood: Int = 3, // 1-5
    val content: String = "",
    val energy: Int = 3, // 1-5
    val timestamp: Long = System.currentTimeMillis()
)

data class HabitWithProgress(
    val habit: Habit,
    val todayCount: Int,
    val streak: Int
)

data class DailySummary(
    val habitsCompleted: Int,
    val habitsTotal: Int,
    val focusMinutes: Int,
    val hasReflection: Boolean,
    val mood: Int?
)
