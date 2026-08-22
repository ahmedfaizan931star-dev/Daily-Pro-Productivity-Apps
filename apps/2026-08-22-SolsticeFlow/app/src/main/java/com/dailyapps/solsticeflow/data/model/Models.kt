package com.dailyapps.solsticeflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val level: Int, // 1-5
    val note: String = ""
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val icon: String = "☀️",
    val streak: Int = 0,
    val lastCompletedDate: String = "", // yyyy-MM-dd
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val energyAtStart: Int = 3
)

@Entity(tableName = "daily_reviews")
data class DailyReview(
    @PrimaryKey val date: String, // yyyy-MM-dd
    val wins: String = "",
    val challenges: String = "",
    val energyAverage: Float = 0f,
    val focusMinutes: Int = 0
)
