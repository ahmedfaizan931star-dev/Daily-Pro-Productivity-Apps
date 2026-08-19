package com.dailyapps.cascadeflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

enum class Priority {
    CRITICAL, HIGH, MEDIUM, LOW
}

enum class EnergyLevel {
    HIGH, MEDIUM, LOW
}

class Converters {
    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromEnergy(value: EnergyLevel): String = value.name

    @TypeConverter
    fun toEnergy(value: String): EnergyLevel = EnergyLevel.valueOf(value)
}

@Entity(tableName = "tasks")
@TypeConverters(Converters::class)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val energyRequired: EnergyLevel = EnergyLevel.MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val streak: Int = 0,
    val lastCompletedDate: String = "", // yyyy-MM-dd
    val isCompletedToday: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val taskId: Long? = null,
    val completedAt: Long = System.currentTimeMillis()
)

data class CascadeStats(
    val tasksCompletedToday: Int = 0,
    val totalTasks: Int = 0,
    val focusMinutesToday: Int = 0,
    val activeHabits: Int = 0,
    val cascadeScore: Int = 0
)
