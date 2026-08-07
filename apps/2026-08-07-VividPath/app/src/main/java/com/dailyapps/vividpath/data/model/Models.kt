package com.dailyapps.vividpath.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PathPriority {
    HIGH, MEDIUM, LOW
}

enum class PathStatus {
    PENDING, IN_PROGRESS, DONE
}

@Entity(tableName = "path_items")
data class PathItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val priority: PathPriority = PathPriority.MEDIUM,
    val status: PathStatus = PathStatus.PENDING,
    val estimatedMinutes: Int = 25,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val dayKey: String // yyyy-MM-dd
)

@Entity(tableName = "daily_intentions")
data class DailyIntention(
    @PrimaryKey val dayKey: String,
    val intention: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pathItemId: Long? = null,
    val durationMinutes: Int,
    val completed: Boolean = false,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long? = null,
    val dayKey: String
)

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey val dayKey: String,
    val mood: Int = 3, // 1-5
    val energy: Int = 3, // 1-5
    val wins: String = "",
    val lessons: String = "",
    val gratitude: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
