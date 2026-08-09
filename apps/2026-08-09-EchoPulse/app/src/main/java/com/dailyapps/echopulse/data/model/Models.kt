package com.dailyapps.echopulse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val icon: String = "check",
    val colorHex: String = "#0EA5E9",
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val completedToday: Boolean = false,
    val lastCompletedDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completed: Boolean = true,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val energyLevel: Int = 3, // 1-5
    val mood: String = "neutral", // great, good, neutral, low, drained
    val date: String, // yyyy-MM-dd
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_pulses")
data class DailyPulse(
    @PrimaryKey val date: String, // yyyy-MM-dd
    val energyScore: Int = 0, // 0-100 derived
    val focusMinutes: Int = 0,
    val habitsCompleted: Int = 0,
    val habitsTotal: Int = 0,
    val reflectionLogged: Boolean = false
)
