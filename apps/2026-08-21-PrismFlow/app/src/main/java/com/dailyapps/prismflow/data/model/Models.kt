package com.dailyapps.prismflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PriorityQuadrant {
    URGENT_IMPORTANT,
    NOT_URGENT_IMPORTANT,
    URGENT_NOT_IMPORTANT,
    NOT_URGENT_NOT_IMPORTANT
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val quadrant: PriorityQuadrant = PriorityQuadrant.URGENT_IMPORTANT,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String = "check",
    val streak: Int = 0,
    val lastCompletedDate: String = "",
    val targetDays: Int = 30
)

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val level: Int, // 1-5
    val note: String = ""
)

data class FocusSession(
    val durationMinutes: Int = 25,
    val isRunning: Boolean = false,
    val remainingSeconds: Int = 25 * 60,
    val linkedTaskId: Long? = null
)
