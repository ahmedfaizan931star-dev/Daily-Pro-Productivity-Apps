package com.dailyapps.ledgermesa.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dailyapps.ledgermesa.data.model.Envelope
import com.dailyapps.ledgermesa.data.model.Spend
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvelopeDao {
    @Query("SELECT * FROM envelopes ORDER BY name")
    fun observe(): Flow<List<Envelope>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Envelope): Long
}

@Dao
interface SpendDao {
    @Query("SELECT * FROM spends ORDER BY createdAt DESC")
    fun observe(): Flow<List<Spend>>

    @Query("SELECT * FROM spends WHERE createdAt >= :since ORDER BY createdAt DESC")
    fun observeSince(since: Long): Flow<List<Spend>>

    @Insert
    suspend fun insert(item: Spend): Long
}

@Database(entities = [Envelope::class, Spend::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun envelopeDao(): EnvelopeDao
    abstract fun spendDao(): SpendDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ledgermesa.db"
                ).build().also { INSTANCE = it }
            }
    }
}
