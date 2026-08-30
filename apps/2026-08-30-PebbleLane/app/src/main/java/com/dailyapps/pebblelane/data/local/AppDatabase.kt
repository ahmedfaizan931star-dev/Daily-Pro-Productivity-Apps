package com.dailyapps.pebblelane.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.pebblelane.data.model.Closeout
import com.dailyapps.pebblelane.data.model.Meeting
import com.dailyapps.pebblelane.data.model.ProtectedBlock
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings ORDER BY createdAt DESC")
    fun observe(): Flow<List<Meeting>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Meeting)
    @Delete
    suspend fun delete(item: Meeting)
}

@Dao
interface BlockDao {
    @Query("SELECT * FROM blocks ORDER BY createdAt DESC")
    fun observe(): Flow<List<ProtectedBlock>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProtectedBlock)
    @Update
    suspend fun update(item: ProtectedBlock)
    @Delete
    suspend fun delete(item: ProtectedBlock)
}

@Dao
interface CloseoutDao {
    @Query("SELECT * FROM closeouts ORDER BY createdAt DESC")
    fun observe(): Flow<List<Closeout>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Closeout)
}

@Database(
    entities = [Meeting::class, ProtectedBlock::class, Closeout::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
    abstract fun blockDao(): BlockDao
    abstract fun closeoutDao(): CloseoutDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pebblelane.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
