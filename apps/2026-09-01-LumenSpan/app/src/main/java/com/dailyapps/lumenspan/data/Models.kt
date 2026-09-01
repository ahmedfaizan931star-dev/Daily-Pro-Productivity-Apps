package com.dailyapps.lumenspan.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EnergyBand { PEAK, STEADY, RECOVERY }

@Entity(tableName = "span_tasks")
data class SpanTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val band: EnergyBand,
    val minutes: Int,
    val done: Boolean = false,
    val dayEpoch: Long
)

@Entity(tableName = "energy_checkins")
data class EnergyCheckin(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val note: String,
    val timestamp: Long
)
