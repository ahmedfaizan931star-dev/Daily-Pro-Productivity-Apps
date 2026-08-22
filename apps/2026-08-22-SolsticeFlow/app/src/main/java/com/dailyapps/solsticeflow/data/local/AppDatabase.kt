package com.dailyapps.solsticeflow.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.solsticeflow.data.model.DailyReview
import com.dailyapps.solsticeflow.data.model.EnergyLog
import com.dailyapps.solsticeflow.data.model.FocusSession
import com.dailyapps.solsticeflow.data.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface EnergyDao {
    @Query("SELECT * FROM energy_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<EnergyLog>>

    @Query("SELECT * FROM energy_logs WHERE timestamp >= :startOfDay ORDER BY timestamp DESC")
    fun getTodayLogs(startOfDay: Long): Flow<List<EnergyLog>>

    @Insert
    suspend fun insert(log: EnergyLog)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAll(): Flow<List<Habit>>

    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC LIMIT 30")
    fun getRecent(): Flow<List<FocusSession>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE completedAt >= :startOfDay")
    fun getTodayMinutes(startOfDay: Long): Flow<Int>

    @Insert
    suspend fun insert(session: FocusSession)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM daily_reviews WHERE date = :date")
    suspend fun getByDate(date: String): DailyReview?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(review: DailyReview)

    @Query("SELECT * FROM daily_reviews ORDER BY date DESC LIMIT 14")
    fun getRecent(): Flow<List<DailyReview>>
}

@Database(
    entities = [EnergyLog::class, Habit::class, FocusSession::class, DailyReview::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun energyDao(): EnergyDao
    abstract fun habitDao(): HabitDao
    abstract fun focusDao(): FocusDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "solsticeflow_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
