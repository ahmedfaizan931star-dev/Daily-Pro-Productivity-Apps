package com.dailyapps.harborrite.data.repository

import android.content.Context
import com.dailyapps.harborrite.data.local.AppDatabase
import com.dailyapps.harborrite.data.model.Berth
import com.dailyapps.harborrite.data.model.Rite
import com.dailyapps.harborrite.data.model.Voyage
import kotlinx.coroutines.flow.Flow

class HarborRepository(context: Context) {
    private val db = AppDatabase.get(context)
    val berths: Flow<List<Berth>> = db.berthDao().observe()
    val voyages: Flow<List<Voyage>> = db.voyageDao().observe()
    val rites: Flow<List<Rite>> = db.riteDao().observe()

    suspend fun addBerth(name: String, intent: String) {
        db.berthDao().upsert(Berth(name = name.trim(), intent = intent.trim()))
    }

    suspend fun deleteBerth(item: Berth) = db.berthDao().delete(item)

    suspend fun addVoyage(berthId: Long, minutes: Int, tide: Int, note: String) {
        db.voyageDao().insert(
            Voyage(
                berthId = berthId,
                minutes = minutes.coerceIn(5, 240),
                tide = tide.coerceIn(1, 5),
                note = note.trim()
            )
        )
    }

    suspend fun deleteVoyage(item: Voyage) = db.voyageDao().delete(item)

    suspend fun addRite(berthId: Long, reflection: String) {
        db.riteDao().insert(Rite(berthId = berthId, reflection = reflection.trim()))
    }

    suspend fun deleteRite(item: Rite) = db.riteDao().delete(item)
}
