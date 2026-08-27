package com.dailyapps.tidequota.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dailyapps.tidequota.data.model.QuotaEntity
import com.dailyapps.tidequota.data.model.TimeBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TideDao {
    @Query("SELECT * FROM quotas")
    fun observeQuotas(): Flow<List<QuotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuota(item: QuotaEntity)

    @Query("SELECT * FROM time_blocks WHERE weekStart = :weekStart ORDER BY createdAt DESC")
    fun observeBlocks(weekStart: Long): Flow<List<TimeBlockEntity>>

    @Insert
    suspend fun insertBlock(item: TimeBlockEntity)

    @Query("DELETE FROM time_blocks WHERE id = :id")
    suspend fun deleteBlock(id: Long)
}

@Database(
    entities = [QuotaEntity::class, TimeBlockEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tideDao(): TideDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tidequota.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
