package com.dailyapps.tidequota.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LifeDomain(val label: String, val defaultHours: Float) {
    DEEP_WORK("Deep Work", 20f),
    LEARNING("Learning", 6f),
    HEALTH("Health", 5f),
    ADMIN("Admin", 6f),
    REST("Rest", 10f),
    PEOPLE("People", 8f)
}

@Entity(tableName = "quotas")
data class QuotaEntity(
    @PrimaryKey val domain: String,
    val weeklyHours: Float
)

@Entity(tableName = "time_blocks")
data class TimeBlockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domain: String,
    val minutes: Int,
    val note: String,
    val createdAt: Long,
    val weekStart: Long
)

data class DomainProgress(
    val domain: LifeDomain,
    val plannedHours: Float,
    val loggedHours: Float
) {
    val remaining: Float get() = (plannedHours - loggedHours).coerceAtLeast(0f)
    val overspend: Float get() = (loggedHours - plannedHours).coerceAtLeast(0f)
    val ratio: Float get() = if (plannedHours <= 0f) 0f else (loggedHours / plannedHours).coerceAtMost(1.6f)
}
