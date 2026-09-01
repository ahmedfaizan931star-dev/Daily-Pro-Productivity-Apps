package com.dailyapps.lumenspan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class BandConverters {
    @TypeConverter
    fun fromBand(value: EnergyBand): String = value.name

    @TypeConverter
    fun toBand(value: String): EnergyBand = EnergyBand.valueOf(value)
}

@Database(entities = [SpanTask::class, EnergyCheckin::class], version = 1, exportSchema = false)
@TypeConverters(BandConverters::class)
abstract class LumenDatabase : RoomDatabase() {
    abstract fun dao(): LumenDao

    companion object {
        @Volatile private var INSTANCE: LumenDatabase? = null

        fun get(context: Context): LumenDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LumenDatabase::class.java,
                    "lumenspan.db"
                ).build().also { INSTANCE = it }
            }
    }
}
