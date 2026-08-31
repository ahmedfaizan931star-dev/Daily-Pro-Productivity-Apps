package com.dailyapps.harborrite.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "berths")
data class Berth(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val intent: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "voyages")
data class Voyage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val berthId: Long,
    val minutes: Int,
    val tide: Int,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "rites")
data class Rite(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val berthId: Long,
    val reflection: String,
    val createdAt: Long = System.currentTimeMillis()
)
