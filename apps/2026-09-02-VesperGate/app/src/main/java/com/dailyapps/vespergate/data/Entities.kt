package com.dailyapps.vespergate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evenings")
data class EveningEntity(
    @PrimaryKey val dateKey: String,
    val note: String = "",
    val intention: String = "",
    val score: Int = 0,
    val ritualsDone: Int = 0,
    val sealed: Boolean = false
)

@Entity(tableName = "loops")
data class LoopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val status: String = "open",
    val createdAt: Long = System.currentTimeMillis()
)
