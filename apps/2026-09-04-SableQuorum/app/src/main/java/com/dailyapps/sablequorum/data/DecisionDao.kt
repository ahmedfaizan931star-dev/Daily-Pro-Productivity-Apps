package com.dailyapps.sablequorum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionDao {
    @Query("SELECT * FROM decisions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Decision>>

    @Query("SELECT * FROM decisions WHERE id = :id")
    suspend fun get(id: Long): Decision?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(decision: Decision): Long

    @Update
    suspend fun update(decision: Decision)

    @Delete
    suspend fun delete(decision: Decision)
}
