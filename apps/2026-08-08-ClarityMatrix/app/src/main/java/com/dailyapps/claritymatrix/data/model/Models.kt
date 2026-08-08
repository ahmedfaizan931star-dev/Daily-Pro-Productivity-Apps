package com.dailyapps.claritymatrix.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Quadrant {
    DO_FIRST,
    SCHEDULE,
    DELEGATE,
    ELIMINATE
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val quadrant: Quadrant = Quadrant.DO_FIRST,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val estimatedMinutes: Int = 25
)

data class DailyStats(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val doFirstCompleted: Int = 0,
    val focusMinutes: Int = 0
)
