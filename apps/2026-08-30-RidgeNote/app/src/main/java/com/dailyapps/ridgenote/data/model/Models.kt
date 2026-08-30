package com.dailyapps.ridgenote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decisions")
data class Decision(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val context: String,
    val choice: String,
    val confidence: Int,
    val domain: String,
    val createdAt: Long = System.currentTimeMillis(),
    val outcome: String = "pending",
    val reviewNote: String = ""
)
