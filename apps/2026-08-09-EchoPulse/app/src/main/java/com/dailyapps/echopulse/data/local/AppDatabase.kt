package com.dailyapps.echopulse.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.echopulse.data.model.DailyPulse
import com.dailyapps.echopulse.data.model.FocusSession
import com.dailyapps.echopulse.data.model.Habit
import com.dailyapps.echopulse.data.model.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE timestamp >= :startOfDay ORDER BY timestamp DESC")
    fun getTodaySessions(startOfDay: Long): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSession): Long
}

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections ORDER BY timestamp DESC")
    fun getAllReflections(): Flow<List<Reflection>>

    @Query("SELECT * FROM reflections WHERE date = :date LIMIT 1")
    fun getReflectionForDate(date: String): Flow<Reflection?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reflection: Reflection): Long

    @Update
    suspend fun update(reflection: Reflection)
}

@Dao
interface DailyPulseDao {
    @Query("SELECT * FROM daily_pulses WHERE date = :date LIMIT 1")
    fun getPulse(date: String): Flow<DailyPulse?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pulse: DailyPulse)
}

@Database(
    entities = [Habit::class, FocusSession::class, Reflection::class, DailyPulse::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun dailyPulseDao(): DailyPulseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "echopulse.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
