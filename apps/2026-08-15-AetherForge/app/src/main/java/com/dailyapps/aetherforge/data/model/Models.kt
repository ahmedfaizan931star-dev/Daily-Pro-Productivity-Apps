package com.dailyapps.aetherforge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EnergyLevel { LOW, MEDIUM, HIGH }
enum class Priority { LOW, MEDIUM, HIGH, CRITICAL }

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val energy: EnergyLevel = EnergyLevel.MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long? = null,
    val durationMinutes: Int,
    val completed: Boolean = true,
    val startedAt: Long = System.currentTimeMillis()
)

data class DailyStats(
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val focusMinutes: Int = 0,
    val streak: Int = 0,
    val energyScore: Int = 70
)
