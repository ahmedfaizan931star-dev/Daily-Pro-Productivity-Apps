package com.dailyapps.quilldeck.data.repository

import android.content.Context
import com.dailyapps.quilldeck.data.local.AppDatabase
import com.dailyapps.quilldeck.data.model.Card
import com.dailyapps.quilldeck.data.model.Deck
import kotlinx.coroutines.flow.Flow

class QuillRepository(context: Context) {
    private val db = AppDatabase.get(context)

    fun decks(): Flow<List<Deck>> = db.deckDao().observeDecks()
    fun cards(): Flow<List<Card>> = db.cardDao().observeCards()
    fun due(now: Long = System.currentTimeMillis()): Flow<List<Card>> = db.cardDao().observeDue(now)

    suspend fun addDeck(name: String) = db.deckDao().insert(Deck(name = name))
    suspend fun deleteDeck(id: Long) = db.deckDao().delete(id)
    suspend fun addCard(deckId: Long, front: String, back: String) =
        db.cardDao().insert(Card(deckId = deckId, front = front, back = back))
    suspend fun updateCard(card: Card) = db.cardDao().update(card)
    suspend fun deleteCard(id: Long) = db.cardDao().delete(id)
}
