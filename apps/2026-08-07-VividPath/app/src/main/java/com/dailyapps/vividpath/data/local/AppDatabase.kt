package com.dailyapps.vividpath.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.vividpath.data.model.DailyIntention
import com.dailyapps.vividpath.data.model.FocusSession
import com.dailyapps.vividpath.data.model.PathItem
import com.dailyapps.vividpath.data.model.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface PathDao {
    @Query("SELECT * FROM path_items WHERE dayKey = :dayKey ORDER BY CASE priority WHEN 'HIGH' THEN 0 WHEN 'MEDIUM' THEN 1 ELSE 2 END, createdAt ASC")
    fun getItemsForDay(dayKey: String): Flow<List<PathItem>>

    @Query("SELECT * FROM path_items WHERE dayKey = :dayKey AND status != 'DONE'")
    fun getPendingItems(dayKey: String): Flow<List<PathItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PathItem): Long

    @Update
    suspend fun updateItem(item: PathItem)

    @Query("DELETE FROM path_items WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("SELECT COUNT(*) FROM path_items WHERE dayKey = :dayKey AND status = 'DONE'")
    fun countCompleted(dayKey: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM path_items WHERE dayKey = :dayKey")
    fun countTotal(dayKey: String): Flow<Int>
}

@Dao
interface IntentionDao {
    @Query("SELECT * FROM daily_intentions WHERE dayKey = :dayKey LIMIT 1")
    fun getIntention(dayKey: String): Flow<DailyIntention?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(intention: DailyIntention)
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions WHERE dayKey = :dayKey ORDER BY startedAt DESC")
    fun getSessionsForDay(dayKey: String): Flow<List<FocusSession>>

    @Insert
    suspend fun insertSession(session: FocusSession): Long

    @Update
    suspend fun updateSession(session: FocusSession)

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE dayKey = :dayKey AND completed = 1")
    fun totalFocusMinutes(dayKey: String): Flow<Int>
}

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections WHERE dayKey = :dayKey LIMIT 1")
    fun getReflection(dayKey: String): Flow<Reflection?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: Reflection)
}

@Database(
    entities = [PathItem::class, DailyIntention::class, FocusSession::class, Reflection::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pathDao(): PathDao
    abstract fun intentionDao(): IntentionDao
    abstract fun focusDao(): FocusDao
    abstract fun reflectionDao(): ReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vividpath.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
