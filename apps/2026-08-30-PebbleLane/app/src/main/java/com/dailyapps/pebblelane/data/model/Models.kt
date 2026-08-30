package com.dailyapps.pebblelane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meetings")
data class Meeting(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val minutes: Int,
    val drain: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "blocks")
data class ProtectedBlock(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val minutes: Int,
    val locked: Boolean = true,
    val done: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "closeouts")
data class Closeout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val wins: String,
    val leftover: String,
    val createdAt: Long = System.currentTimeMillis()
)
