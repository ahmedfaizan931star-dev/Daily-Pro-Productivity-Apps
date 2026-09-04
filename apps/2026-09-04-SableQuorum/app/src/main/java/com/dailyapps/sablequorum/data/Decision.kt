package com.dailyapps.sablequorum.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decisions")
data class Decision(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val context: String,
    val options: String,
    val criteria: String,
    val chosen: String,
    val expectedOutcome: String,
    val domain: String,
    val urgency: Int,
    val confidence: Int,
    val status: String,
    val reviewNotes: String,
    val createdAt: Long,
    val reviewedAt: Long?
)
