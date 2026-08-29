package com.dailyapps.kitebrief.data.repository

import com.dailyapps.kitebrief.data.local.AppDatabase
import com.dailyapps.kitebrief.data.model.Brief
import com.dailyapps.kitebrief.data.model.Commitment
import kotlinx.coroutines.flow.Flow

class KiteRepository(db: AppDatabase) {
    private val briefs = db.briefDao()
    private val commitments = db.commitmentDao()

    fun observeBrief(dateKey: String): Flow<Brief?> = briefs.observeBrief(dateKey)
    fun observeRecent(): Flow<List<Brief>> = briefs.observeRecent()
    fun observeCommitments(dateKey: String): Flow<List<Commitment>> = commitments.observeForDay(dateKey)

    suspend fun saveBrief(brief: Brief) = briefs.upsert(brief)
    suspend fun addCommitment(item: Commitment) = commitments.insert(item)
    suspend fun updateCommitment(item: Commitment) = commitments.update(item)
    suspend fun deleteCommitment(id: Long) = commitments.delete(id)
}
