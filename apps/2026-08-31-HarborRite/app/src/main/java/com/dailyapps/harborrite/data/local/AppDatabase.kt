package com.dailyapps.harborrite.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dailyapps.harborrite.data.model.Berth
import com.dailyapps.harborrite.data.model.Rite
import com.dailyapps.harborrite.data.model.Voyage
import kotlinx.coroutines.flow.Flow

@Dao
interface BerthDao {
    @Query("SELECT * FROM berths ORDER BY createdAt DESC")
    fun observe(): Flow<List<Berth>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Berth): Long

    @Delete
    suspend fun delete(item: Berth)
}

@Dao
interface VoyageDao {
    @Query("SELECT * FROM voyages ORDER BY createdAt DESC")
    fun observe(): Flow<List<Voyage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Voyage): Long

    @Delete
    suspend fun delete(item: Voyage)
}

@Dao
interface RiteDao {
    @Query("SELECT * FROM rites ORDER BY createdAt DESC")
    fun observe(): Flow<List<Rite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Rite): Long

    @Delete
    suspend fun delete(item: Rite)
}

@Database(entities = [Berth::class, Voyage::class, Rite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun berthDao(): BerthDao
    abstract fun voyageDao(): VoyageDao
    abstract fun riteDao(): RiteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "harborrite.db"
                ).build().also { INSTANCE = it }
            }
    }
}
