package com.dailyapps.lumenspan.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LumenDao {
    @Query("SELECT * FROM span_tasks ORDER BY done ASC, band ASC, id DESC")
    fun observeTasks(): Flow<List<SpanTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: SpanTask)

    @Update
    suspend fun updateTask(task: SpanTask)

    @Delete
    suspend fun deleteTask(task: SpanTask)

    @Query("SELECT * FROM energy_checkins ORDER BY timestamp DESC")
    fun observeCheckins(): Flow<List<EnergyCheckin>>

    @Insert
    suspend fun insertCheckin(checkin: EnergyCheckin)
}
