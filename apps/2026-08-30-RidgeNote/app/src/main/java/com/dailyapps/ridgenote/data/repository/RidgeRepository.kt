package com.dailyapps.ridgenote.data.repository

import com.dailyapps.ridgenote.data.local.DecisionDao
import com.dailyapps.ridgenote.data.model.Decision
import kotlinx.coroutines.flow.Flow

class RidgeRepository(private val dao: DecisionDao) {
    fun observe(): Flow<List<Decision>> = dao.observeAll()
    suspend fun add(item: Decision) { dao.insert(item) }
    suspend fun update(item: Decision) { dao.update(item) }
    suspend fun delete(item: Decision) { dao.delete(item) }
}
