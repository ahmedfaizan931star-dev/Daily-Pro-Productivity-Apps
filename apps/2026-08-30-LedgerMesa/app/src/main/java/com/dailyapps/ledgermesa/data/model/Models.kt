package com.dailyapps.ledgermesa.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "envelopes")
data class Envelope(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val weeklyLimitCents: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "spends")
data class Spend(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val envelopeId: Long,
    val amountCents: Int,
    val note: String,
    val createdAt: Long = System.currentTimeMillis()
)
