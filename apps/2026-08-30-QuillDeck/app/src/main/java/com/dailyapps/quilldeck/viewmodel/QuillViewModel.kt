package com.dailyapps.quilldeck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.quilldeck.data.model.Card
import com.dailyapps.quilldeck.data.model.Deck
import com.dailyapps.quilldeck.data.repository.QuillRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

data class QuillUiState(
    val decks: List<Deck> = emptyList(),
    val cards: List<Card> = emptyList(),
    val due: List<Card> = emptyList(),
    val reviewedToday: Int = 0,
    val retentionPct: Int = 0
)

class QuillViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = QuillRepository(app)

    val ui = combine(repo.decks(), repo.cards(), repo.due()) { decks, cards, due ->
        val reviewed = cards.count { it.reviews > 0 }
        val lapses = cards.sumOf { it.lapses }
        val retention = if (reviewed == 0) 0 else ((1f - lapses.toFloat() / max(reviewed, 1)) * 100).roundToInt().coerceIn(0, 100)
        QuillUiState(decks, cards, due, reviewed, retention)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QuillUiState())

    fun addDeck(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) repo.addDeck(name.trim())
    }

    fun addCard(deckId: Long, front: String, back: String) = viewModelScope.launch {
        if (front.isNotBlank() && back.isNotBlank()) repo.addCard(deckId, front.trim(), back.trim())
    }

    fun review(card: Card, quality: Int) = viewModelScope.launch {
        val dayMs = 24L * 60 * 60 * 1000
        val q = quality.coerceIn(0, 5)
        val ease = (card.ease + (0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f))).coerceAtLeast(1.3f)
        val interval = when {
            q < 3 -> 1
            card.intervalDays == 0 -> 1
            card.intervalDays == 1 -> 3
            else -> max(1, (card.intervalDays * ease).roundToInt())
        }
        repo.updateCard(
            card.copy(
                ease = ease,
                intervalDays = interval,
                dueAt = System.currentTimeMillis() + interval * dayMs,
                reviews = card.reviews + 1,
                lapses = card.lapses + if (q < 3) 1 else 0
            )
        )
    }
}
