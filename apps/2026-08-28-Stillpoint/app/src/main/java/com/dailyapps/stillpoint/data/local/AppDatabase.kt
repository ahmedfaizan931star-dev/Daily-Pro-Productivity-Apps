package com.dailyapps.stillpoint.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.stillpoint.data.model.FocusSession
import com.dailyapps.stillpoint.data.model.Intention
import com.dailyapps.stillpoint.data.model.QuietBlock
import kotlinx.coroutines.flow.Flow

@Dao
interface StillDao {
    @Query("SELECT * FROM quiet_blocks ORDER BY startHour")
    fun observeBlocks(): Flow<List<QuietBlock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: QuietBlock)

    @Update
    suspend fun updateBlock(block: QuietBlock)

    @Delete
    suspend fun deleteBlock(block: QuietBlock)

    @Query("SELECT * FROM intentions ORDER BY createdAt DESC")
    fun observeIntentions(): Flow<List<Intention>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntention(intention: Intention)

    @Update
    suspend fun updateIntention(intention: Intention)

    @Delete
    suspend fun deleteIntention(intention: Intention)

    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun observeSessions(): Flow<List<FocusSession>>

    @Insert
    suspend fun insertSession(session: FocusSession)
}

@Database(
    entities = [QuietBlock::class, Intention::class, FocusSession::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): StillDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stillpoint.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
