package com.dailyapps.luminafocus.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.luminafocus.data.model.EnergyLog
import com.dailyapps.luminafocus.data.model.FocusSession
import com.dailyapps.luminafocus.data.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun getAll(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE completedAt >= :startOfDay ORDER BY completedAt DESC")
    fun getToday(startOfDay: Long): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSession)

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE completedAt >= :startOfDay")
    fun getTodayMinutes(startOfDay: Long): Flow<Int>
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAll(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface EnergyLogDao {
    @Query("SELECT * FROM energy_logs ORDER BY loggedAt DESC")
    fun getAll(): Flow<List<EnergyLog>>

    @Query("SELECT * FROM energy_logs WHERE loggedAt >= :startOfDay ORDER BY loggedAt DESC")
    fun getToday(startOfDay: Long): Flow<List<EnergyLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: EnergyLog)
}

@Database(
    entities = [FocusSession::class, Habit::class, EnergyLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun habitDao(): HabitDao
    abstract fun energyLogDao(): EnergyLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lumina_focus.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
