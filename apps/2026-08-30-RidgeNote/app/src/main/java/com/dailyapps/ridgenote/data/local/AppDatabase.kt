package com.dailyapps.ridgenote.data.local

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
import com.dailyapps.ridgenote.data.model.Decision
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionDao {
    @Query("SELECT * FROM decisions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Decision>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Decision): Long

    @Update
    suspend fun update(item: Decision)

    @Delete
    suspend fun delete(item: Decision)
}

@Database(entities = [Decision::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun decisionDao(): DecisionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ridgenote.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
