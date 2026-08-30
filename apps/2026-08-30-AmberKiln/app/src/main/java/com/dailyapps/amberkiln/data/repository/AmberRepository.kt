package com.dailyapps.amberkiln.data.repository

import android.content.Context
import com.dailyapps.amberkiln.data.local.AppDatabase
import com.dailyapps.amberkiln.data.model.Cooldown
import com.dailyapps.amberkiln.data.model.FireSession
import com.dailyapps.amberkiln.data.model.Kiln
import kotlinx.coroutines.flow.Flow

class AmberRepository(context: Context) {
    private val db = AppDatabase.get(context)
    val kilns: Flow<List<Kiln>> = db.kilnDao().observe()
    val sessions: Flow<List<FireSession>> = db.sessionDao().observe()
    val cooldowns: Flow<List<Cooldown>> = db.cooldownDao().observe()

    suspend fun addKiln(name: String, intent: String) {
        db.kilnDao().upsert(Kiln(name = name.trim(), intent = intent.trim()))
    }

    suspend fun deleteKiln(item: Kiln) = db.kilnDao().delete(item)

    suspend fun addSession(kilnId: Long, minutes: Int, intensity: Int, note: String) {
        db.sessionDao().insert(
            FireSession(
                kilnId = kilnId,
                minutes = minutes.coerceIn(5, 240),
                intensity = intensity.coerceIn(1, 5),
                note = note.trim()
            )
        )
    }

    suspend fun deleteSession(item: FireSession) = db.sessionDao().delete(item)

    suspend fun addCooldown(kilnId: Long, reflection: String) {
        db.cooldownDao().insert(Cooldown(kilnId = kilnId, reflection = reflection.trim()))
    }

    suspend fun deleteCooldown(item: Cooldown) = db.cooldownDao().delete(item)
}
