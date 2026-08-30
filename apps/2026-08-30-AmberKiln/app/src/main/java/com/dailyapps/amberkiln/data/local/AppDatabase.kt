package com.dailyapps.amberkiln.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dailyapps.amberkiln.data.model.Cooldown
import com.dailyapps.amberkiln.data.model.FireSession
import com.dailyapps.amberkiln.data.model.Kiln
import kotlinx.coroutines.flow.Flow

@Dao
interface KilnDao {
    @Query("SELECT * FROM kilns ORDER BY createdAt DESC")
    fun observe(): Flow<List<Kiln>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Kiln): Long

    @Delete
    suspend fun delete(item: Kiln)
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM fire_sessions ORDER BY createdAt DESC")
    fun observe(): Flow<List<FireSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FireSession): Long

    @Delete
    suspend fun delete(item: FireSession)
}

@Dao
interface CooldownDao {
    @Query("SELECT * FROM cooldowns ORDER BY createdAt DESC")
    fun observe(): Flow<List<Cooldown>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Cooldown): Long

    @Delete
    suspend fun delete(item: Cooldown)
}

@Database(entities = [Kiln::class, FireSession::class, Cooldown::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kilnDao(): KilnDao
    abstract fun sessionDao(): SessionDao
    abstract fun cooldownDao(): CooldownDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "amberkiln.db"
                ).build().also { INSTANCE = it }
            }
    }
}
