package com.dailyapps.vespergate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EveningDao {
    @Query("SELECT * FROM evenings ORDER BY dateKey DESC")
    fun observeAll(): Flow<List<EveningEntity>>

    @Query("SELECT * FROM evenings WHERE dateKey = :key LIMIT 1")
    suspend fun get(key: String): EveningEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: EveningEntity)
}

@Dao
interface LoopDao {
    @Query("SELECT * FROM loops ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<LoopEntity>>

    @Insert
    suspend fun insert(entity: LoopEntity)

    @Update
    suspend fun update(entity: LoopEntity)

    @Delete
    suspend fun delete(entity: LoopEntity)
}
