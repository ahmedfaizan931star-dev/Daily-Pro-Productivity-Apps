package com.dailyapps.momentumvault.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.momentumvault.data.model.EnergyLog
import com.dailyapps.momentumvault.data.model.FocusSession
import com.dailyapps.momentumvault.data.model.Habit
import com.dailyapps.momentumvault.data.model.HabitLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<HabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLog(habitId: Long, date: String)
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE completedAt >= :startOfDay ORDER BY completedAt DESC")
    fun getSessionsSince(startOfDay: Long): Flow<List<FocusSession>>

    @Insert
    suspend fun insertSession(session: FocusSession): Long

    @Query("DELETE FROM focus_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)
}

@Dao
interface EnergyDao {
    @Query("SELECT * FROM energy_logs ORDER BY createdAt DESC")
    fun getAllLogs(): Flow<List<EnergyLog>>

    @Query("SELECT * FROM energy_logs WHERE date = :date LIMIT 1")
    fun getLogForDate(date: String): Flow<EnergyLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: EnergyLog): Long

    @Update
    suspend fun updateLog(log: EnergyLog)
}

@Database(
    entities = [Habit::class, HabitLog::class, FocusSession::class, EnergyLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun focusDao(): FocusDao
    abstract fun energyDao(): EnergyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "momentum_vault.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
