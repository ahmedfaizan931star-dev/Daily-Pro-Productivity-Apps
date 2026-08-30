package com.dailyapps.covedraft.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class Draft(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val note: String = "",
    val parked: Boolean = true,
    val launched: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "decisions")
data class Decision(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val request: String,
    val verdict: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "closeouts")
data class Closeout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val win: String,
    val leftover: String,
    val createdAt: Long = System.currentTimeMillis()
)
