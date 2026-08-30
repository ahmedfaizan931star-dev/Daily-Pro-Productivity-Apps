package com.dailyapps.quilldeck.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.dailyapps.quilldeck.data.model.Card
import com.dailyapps.quilldeck.data.model.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks ORDER BY createdAt DESC")
    fun observeDecks(): Flow<List<Deck>>

    @Insert
    suspend fun insert(deck: Deck): Long

    @Query("DELETE FROM decks WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY dueAt ASC")
    fun observeCards(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE dueAt <= :now ORDER BY dueAt ASC")
    fun observeDue(now: Long): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card): Long

    @Update
    suspend fun update(card: Card)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun delete(id: Long)
}

@Database(entities = [Deck::class, Card::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quilldeck.db"
                ).build().also { INSTANCE = it }
            }
    }
}
