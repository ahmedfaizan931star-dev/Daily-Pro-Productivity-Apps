package com.dailyapps.aetherforge.data.local

import androidx.room.TypeConverter
import com.dailyapps.aetherforge.data.model.EnergyLevel
import com.dailyapps.aetherforge.data.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = try {
        Priority.valueOf(value)
    } catch (e: Exception) {
        Priority.MEDIUM
    }

    @TypeConverter
    fun fromEnergy(value: EnergyLevel): String = value.name

    @TypeConverter
    fun toEnergy(value: String): EnergyLevel = try {
        EnergyLevel.valueOf(value)
    } catch (e: Exception) {
        EnergyLevel.MEDIUM
    }
}
