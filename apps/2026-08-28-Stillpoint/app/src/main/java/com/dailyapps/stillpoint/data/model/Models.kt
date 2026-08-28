package com.dailyapps.stillpoint.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiet_blocks")
data class QuietBlock(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startHour: Int,
    val durationMin: Int,
    val weekdayMask: Int,
    val enabled: Boolean = true
)

@Entity(tableName = "intentions")
data class Intention(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val energy: Int,
    val createdAt: Long,
    val completed: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val minutes: Int,
    val completedAt: Long,
    val note: String
)
