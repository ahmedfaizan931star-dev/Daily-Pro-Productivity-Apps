package com.dailyapps.pulseforge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val iconName: String = "check",
    val colorHex: String = "#4F46E5",
    val targetPerDay: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "habit_completions")
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val dateEpochDay: Long, // LocalDate.toEpochDay()
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val mode: String, // "classic", "deep", "custom"
    val completedAt: Long = System.currentTimeMillis(),
    val wasCompleted: Boolean = true
)

@Entity(tableName = "priorities")
data class Priority(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val dateEpochDay: Long,
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class FocusMode(val workMinutes: Int, val breakMinutes: Int, val label: String) {
    CLASSIC(25, 5, "Classic 25/5"),
    DEEP(50, 10, "Deep Work 50/10"),
    LONG(90, 15, "Long Form 90/15"),
    CUSTOM(25, 5, "Custom")
}
