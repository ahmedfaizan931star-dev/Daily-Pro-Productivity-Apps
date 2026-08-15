package com.dailyapps.aetherforge.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.dailyapps.aetherforge.data.model.FocusSessionEntity
import com.dailyapps.aetherforge.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, priority DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY priority DESC, createdAt DESC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND completedAt >= :startOfDay")
    suspend fun countCompletedToday(startOfDay: Long): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE createdAt >= :startOfDay")
    suspend fun countCreatedToday(startOfDay: Long): Int
}

@Dao
interface FocusDao {
    @Insert
    suspend fun insert(session: FocusSessionEntity): Long

    @Query("SELECT * FROM focus_sessions ORDER BY startedAt DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE startedAt >= :startOfDay AND completed = 1")
    suspend fun totalMinutesToday(startOfDay: Long): Int
}

@Database(entities = [TaskEntity::class, FocusSessionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun focusDao(): FocusDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aetherforge.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
