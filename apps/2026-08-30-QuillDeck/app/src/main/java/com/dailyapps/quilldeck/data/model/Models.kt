package com.dailyapps.quilldeck.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckId: Long,
    val front: String,
    val back: String,
    val intervalDays: Int = 0,
    val ease: Float = 2.5f,
    val dueAt: Long = System.currentTimeMillis(),
    val reviews: Int = 0,
    val lapses: Int = 0
)
