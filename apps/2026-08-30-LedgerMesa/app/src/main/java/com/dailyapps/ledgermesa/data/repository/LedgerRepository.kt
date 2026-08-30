package com.dailyapps.ledgermesa.data.repository

import android.app.Application
import com.dailyapps.ledgermesa.data.local.AppDatabase
import com.dailyapps.ledgermesa.data.model.Envelope
import com.dailyapps.ledgermesa.data.model.Spend
import kotlinx.coroutines.flow.Flow

class LedgerRepository(app: Application) {
    private val db = AppDatabase.get(app)
    val envelopes: Flow<List<Envelope>> = db.envelopeDao().observe()
    val spends: Flow<List<Spend>> = db.spendDao().observe()

    suspend fun addEnvelope(name: String, weeklyLimitCents: Int) {
        db.envelopeDao().upsert(Envelope(name = name, weeklyLimitCents = weeklyLimitCents))
    }

    suspend fun addSpend(envelopeId: Long, amountCents: Int, note: String) {
        db.spendDao().insert(Spend(envelopeId = envelopeId, amountCents = amountCents, note = note))
    }
}
