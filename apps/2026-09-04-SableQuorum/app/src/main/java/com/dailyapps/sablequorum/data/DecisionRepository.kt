package com.dailyapps.sablequorum.data

import kotlinx.coroutines.flow.Flow

class DecisionRepository(private val dao: DecisionDao) {
    fun observe(): Flow<List<Decision>> = dao.observeAll()

    suspend fun save(decision: Decision): Long = dao.upsert(decision)

    suspend fun update(decision: Decision) = dao.update(decision)

    suspend fun delete(decision: Decision) = dao.delete(decision)
}
