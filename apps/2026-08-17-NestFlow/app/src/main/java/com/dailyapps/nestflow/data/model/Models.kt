package com.dailyapps.nestflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nests")
data class Nest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val colorHex: String = "#0F766E",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nestId: Long,
    val title: String,
    val streak: Int = 0,
    val lastCompletedDate: String = "", // yyyy-MM-dd
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nestId: Long,
    val title: String,
    val isDone: Boolean = false,
    val priority: Int = 1, // 1 low, 2 med, 3 high
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nestId: Long? = null,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val level: Int, // 1-5
    val loggedAt: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val mood: Int = 3, // 1-5
    val createdAt: Long = System.currentTimeMillis()
)
