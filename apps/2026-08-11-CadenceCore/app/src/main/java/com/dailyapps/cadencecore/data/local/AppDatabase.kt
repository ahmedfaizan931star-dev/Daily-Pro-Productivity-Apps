package com.dailyapps.cadencecore.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.cadencecore.data.model.FocusSession
import com.dailyapps.cadencecore.data.model.Habit
import com.dailyapps.cadencecore.data.model.HabitLog
import com.dailyapps.cadencecore.data.model.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND dateKey = :dateKey LIMIT 1")
    suspend fun getLogForDay(habitId: Long, dateKey: String): HabitLog?

    @Query("SELECT * FROM habit_logs WHERE dateKey = :dateKey")
    fun getLogsForDay(dateKey: String): Flow<List<HabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY dateKey DESC")
    suspend fun getLogsForHabit(habitId: Long): List<HabitLog>
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE dateKey = :dateKey")
    fun getSessionsForDay(dateKey: String): Flow<List<FocusSession>>

    @Insert
    suspend fun insertSession(session: FocusSession): Long

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE dateKey = :dateKey AND completed = 1")
    fun getTotalMinutesForDay(dateKey: String): Flow<Int>
}

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections ORDER BY dateKey DESC")
    fun getAllReflections(): Flow<List<Reflection>>

    @Query("SELECT * FROM reflections WHERE dateKey = :dateKey LIMIT 1")
    fun getReflectionForDay(dateKey: String): Flow<Reflection?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: Reflection): Long

    @Update
    suspend fun updateReflection(reflection: Reflection)
}

@Database(
    entities = [Habit::class, HabitLog::class, FocusSession::class, Reflection::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
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
                    "cadence_core.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
