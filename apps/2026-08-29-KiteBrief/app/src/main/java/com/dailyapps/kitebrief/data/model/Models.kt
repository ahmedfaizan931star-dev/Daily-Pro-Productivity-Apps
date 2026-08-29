package com.dailyapps.kitebrief.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "briefs")
data class Brief(
    @PrimaryKey val dateKey: String,
    val intention: String = "",
    val energy: Int = 3,
    val shutdownNote: String = "",
    val landingScore: Int = 0,
    val shutDown: Boolean = false
)

@Entity(tableName = "commitments")
data class Commitment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val title: String,
    val done: Boolean = false
)
