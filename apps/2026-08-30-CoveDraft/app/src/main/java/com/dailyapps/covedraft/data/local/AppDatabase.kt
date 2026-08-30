package com.dailyapps.covedraft.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.covedraft.data.model.Closeout
import com.dailyapps.covedraft.data.model.Decision
import com.dailyapps.covedraft.data.model.Draft
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftDao {
    @Query("SELECT * FROM drafts ORDER BY createdAt DESC")
    fun observe(): Flow<List<Draft>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(item: Draft)
    @Update suspend fun update(item: Draft)
    @Query("DELETE FROM drafts WHERE id = :id") suspend fun delete(id: Long)
}

@Dao
interface DecisionDao {
    @Query("SELECT * FROM decisions ORDER BY createdAt DESC")
    fun observe(): Flow<List<Decision>>
    @Insert suspend fun insert(item: Decision)
    @Query("DELETE FROM decisions WHERE id = :id") suspend fun delete(id: Long)
}

@Dao
interface CloseoutDao {
    @Query("SELECT * FROM closeouts ORDER BY createdAt DESC")
    fun observe(): Flow<List<Closeout>>
    @Insert suspend fun insert(item: Closeout)
}

@Database(entities = [Draft::class, Decision::class, Closeout::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun draftDao(): DraftDao
    abstract fun decisionDao(): DecisionDao
    abstract fun closeoutDao(): CloseoutDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "covedraft.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
