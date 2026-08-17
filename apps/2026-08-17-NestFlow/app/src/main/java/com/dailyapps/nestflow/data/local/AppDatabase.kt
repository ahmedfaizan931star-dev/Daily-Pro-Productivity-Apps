package com.dailyapps.nestflow.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.nestflow.data.model.EnergyLog
import com.dailyapps.nestflow.data.model.FocusSession
import com.dailyapps.nestflow.data.model.Habit
import com.dailyapps.nestflow.data.model.Nest
import com.dailyapps.nestflow.data.model.Reflection
import com.dailyapps.nestflow.data.model.TaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NestDao {
    @Query("SELECT * FROM nests ORDER BY createdAt DESC")
    fun getAllNests(): Flow<List<Nest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNest(nest: Nest): Long

    @Query("DELETE FROM nests WHERE id = :id")
    suspend fun deleteNest(id: Long)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE nestId = :nestId")
    fun getHabitsForNest(nestId: Long): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY priority DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<TaskItem>>

    @Query("SELECT * FROM tasks WHERE nestId = :nestId AND isDone = 0")
    fun getOpenTasksForNest(nestId: Long): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem): Long

    @Update
    suspend fun updateTask(task: TaskItem)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSession): Long

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE completedAt >= :since")
    fun totalMinutesSince(since: Long): Flow<Int?>
}

@Dao
interface EnergyDao {
    @Query("SELECT * FROM energy_logs ORDER BY loggedAt DESC LIMIT 30")
    fun getRecentEnergy(): Flow<List<EnergyLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnergy(log: EnergyLog): Long

    @Query("SELECT * FROM energy_logs ORDER BY loggedAt DESC LIMIT 1")
    fun latestEnergy(): Flow<EnergyLog?>
}

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections ORDER BY createdAt DESC")
    fun getAllReflections(): Flow<List<Reflection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: Reflection): Long
}

@Database(
    entities = [Nest::class, Habit::class, TaskItem::class, FocusSession::class, EnergyLog::class, Reflection::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nestDao(): NestDao
    abstract fun habitDao(): HabitDao
    abstract fun taskDao(): TaskDao
    abstract fun focusDao(): FocusDao
    abstract fun energyDao(): EnergyDao
    abstract fun reflectionDao(): ReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nestflow.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
