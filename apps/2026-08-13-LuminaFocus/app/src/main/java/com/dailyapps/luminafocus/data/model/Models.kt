package com.dailyapps.luminafocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val mode: String = "pomodoro" // pomodoro, deep, custom
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val icon: String = "⭐",
    val streak: Int = 0,
    val lastCompletedDate: String? = null, // yyyy-MM-dd
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val energyLevel: Int, // 1-5
    val mood: Int, // 1-5
    val note: String = "",
    val loggedAt: Long = System.currentTimeMillis()
)

data class DailyStats(
    val focusMinutesToday: Int = 0,
    val sessionsToday: Int = 0,
    val habitsCompletedToday: Int = 0,
    val totalHabits: Int = 0,
    val avgEnergy: Float = 0f,
    val avgMood: Float = 0f
)
