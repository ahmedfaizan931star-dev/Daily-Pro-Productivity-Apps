package com.dailyapps.amberkiln.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kilns")
data class Kiln(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val intent: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fire_sessions")
data class FireSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kilnId: Long,
    val minutes: Int,
    val intensity: Int,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cooldowns")
data class Cooldown(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kilnId: Long,
    val reflection: String,
    val createdAt: Long = System.currentTimeMillis()
)
