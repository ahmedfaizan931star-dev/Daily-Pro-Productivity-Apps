package com.dailyapps.stillpoint.data.repository

import android.content.Context
import com.dailyapps.stillpoint.data.local.AppDatabase
import com.dailyapps.stillpoint.data.model.FocusSession
import com.dailyapps.stillpoint.data.model.Intention
import com.dailyapps.stillpoint.data.model.QuietBlock
import kotlinx.coroutines.flow.Flow

class StillRepository(context: Context) {
    private val dao = AppDatabase.get(context).dao()

    val blocks: Flow<List<QuietBlock>> = dao.observeBlocks()
    val intentions: Flow<List<Intention>> = dao.observeIntentions()
    val sessions: Flow<List<FocusSession>> = dao.observeSessions()

    suspend fun addBlock(title: String, startHour: Int, durationMin: Int) {
        dao.insertBlock(
            QuietBlock(
                title = title,
                startHour = startHour,
                durationMin = durationMin,
                weekdayMask = 31
            )
        )
    }

    suspend fun toggleBlock(block: QuietBlock) {
        dao.updateBlock(block.copy(enabled = !block.enabled))
    }

    suspend fun deleteBlock(block: QuietBlock) = dao.deleteBlock(block)

    suspend fun addIntention(text: String, energy: Int) {
        dao.insertIntention(
            Intention(
                text = text,
                energy = energy.coerceIn(1, 5),
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleIntention(item: Intention) {
        dao.updateIntention(item.copy(completed = !item.completed))
    }

    suspend fun deleteIntention(item: Intention) = dao.deleteIntention(item)

    suspend fun logSession(minutes: Int, note: String) {
        dao.insertSession(
            FocusSession(
                minutes = minutes,
                completedAt = System.currentTimeMillis(),
                note = note
            )
        )
    }
}
