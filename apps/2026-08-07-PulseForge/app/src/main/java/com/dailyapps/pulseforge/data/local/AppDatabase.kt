package com.dailyapps.pulseforge.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.dailyapps.pulseforge.data.model.FocusSession
import com.dailyapps.pulseforge.data.model.Habit
import com.dailyapps.pulseforge.data.model.HabitCompletion
import com.dailyapps.pulseforge.data.model.Priority
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit_completions WHERE dateEpochDay = :day")
    fun getCompletionsForDay(day: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY dateEpochDay DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletion)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND dateEpochDay = :day")
    suspend fun removeCompletion(habitId: Long, day: Long)

    @Query("SELECT COUNT(*) FROM habit_completions WHERE habitId = :habitId")
    suspend fun getCompletionCount(habitId: Long): Int
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE completedAt >= :since ORDER BY completedAt DESC")
    fun getSessionsSince(since: Long): Flow<List<FocusSession>>

    @Insert
    suspend fun insertSession(session: FocusSession): Long

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE completedAt >= :since AND wasCompleted = 1")
    suspend fun getTotalFocusMinutesSince(since: Long): Int?
}

@Dao
interface PriorityDao {
    @Query("SELECT * FROM priorities WHERE dateEpochDay = :day ORDER BY orderIndex ASC")
    fun getPrioritiesForDay(day: Long): Flow<List<Priority>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriority(priority: Priority): Long

    @Update
    suspend fun updatePriority(priority: Priority)

    @Delete
    suspend fun deletePriority(priority: Priority)

    @Query("DELETE FROM priorities WHERE dateEpochDay = :day")
    suspend fun clearDay(day: Long)
}

@Database(
    entities = [Habit::class, HabitCompletion::class, FocusSession::class, Priority::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun focusDao(): FocusDao
    abstract fun priorityDao(): PriorityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pulseforge.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
