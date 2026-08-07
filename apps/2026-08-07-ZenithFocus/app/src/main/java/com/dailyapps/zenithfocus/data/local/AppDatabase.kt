package com.dailyapps.zenithfocus.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.zenithfocus.data.model.DailyStat
import com.dailyapps.zenithfocus.data.model.FocusSession
import com.dailyapps.zenithfocus.data.model.Reflection
import com.dailyapps.zenithfocus.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getOpenTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND date(completedAt / 1000, 'unixepoch') = date('now')")
    fun completedTodayCount(): Flow<Int>
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<FocusSession>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE date(completedAt / 1000, 'unixepoch') = date('now')")
    fun focusMinutesToday(): Flow<Int>

    @Insert
    suspend fun insert(session: FocusSession): Long
}

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections WHERE dateKey = :dateKey LIMIT 1")
    fun getForDate(dateKey: String): Flow<Reflection?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: Reflection)

    @Query("SELECT * FROM reflections ORDER BY dateKey DESC LIMIT 14")
    fun getRecent(): Flow<List<Reflection>>
}

@Dao
interface DailyStatDao {
    @Query("SELECT * FROM daily_stats WHERE dateKey = :dateKey LIMIT 1")
    fun getForDate(dateKey: String): Flow<DailyStat?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stat: DailyStat)
}

@Database(
    entities = [Task::class, FocusSession::class, Reflection::class, DailyStat::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun dailyStatDao(): DailyStatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zenith_focus.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
