package com.dailyapps.zenithfocus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EnergyLevel { HIGH, MEDIUM, LOW }

enum class MatrixQuadrant {
    DO_FIRST,   // Urgent + Important
    SCHEDULE,   // Not urgent + Important
    DELEGATE,   // Urgent + Not important
    ELIMINATE   // Neither
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val quadrant: MatrixQuadrant = MatrixQuadrant.DO_FIRST,
    val energyRequired: EnergyLevel = EnergyLevel.MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val mode: String, // "Pomodoro", "Deep", "Custom"
    val completedAt: Long = System.currentTimeMillis(),
    val taskId: Long? = null
)

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String, // yyyy-MM-dd
    val energyMorning: EnergyLevel? = null,
    val energyAfternoon: EnergyLevel? = null,
    val energyEvening: EnergyLevel? = null,
    val wins: String = "",
    val challenges: String = "",
    val gratitude: String = "",
    val tomorrowIntention: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_stats")
data class DailyStat(
    @PrimaryKey val dateKey: String,
    val focusMinutes: Int = 0,
    val tasksCompleted: Int = 0,
    val tasksTotal: Int = 0
)
