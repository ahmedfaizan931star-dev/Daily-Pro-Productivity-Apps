package com.dailyapps.kitebrief.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.kitebrief.data.model.Brief
import com.dailyapps.kitebrief.data.model.Commitment
import kotlinx.coroutines.flow.Flow

@Dao
interface BriefDao {
    @Query("SELECT * FROM briefs WHERE dateKey = :key LIMIT 1")
    fun observeBrief(key: String): Flow<Brief?>

    @Query("SELECT * FROM briefs ORDER BY dateKey DESC LIMIT 14")
    fun observeRecent(): Flow<List<Brief>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(brief: Brief)
}

@Dao
interface CommitmentDao {
    @Query("SELECT * FROM commitments WHERE dateKey = :key ORDER BY id ASC")
    fun observeForDay(key: String): Flow<List<Commitment>>

    @Insert
    suspend fun insert(item: Commitment)

    @Update
    suspend fun update(item: Commitment)

    @Query("DELETE FROM commitments WHERE id = :id")
    suspend fun delete(id: Long)
}

@Database(entities = [Brief::class, Commitment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun briefDao(): BriefDao
    abstract fun commitmentDao(): CommitmentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kitebrief.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
