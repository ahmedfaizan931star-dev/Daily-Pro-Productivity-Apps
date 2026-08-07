package com.dailyapps.vividpath.data.repository

import android.content.Context
import com.dailyapps.vividpath.data.local.AppDatabase
import com.dailyapps.vividpath.data.model.DailyIntention
import com.dailyapps.vividpath.data.model.FocusSession
import com.dailyapps.vividpath.data.model.PathItem
import com.dailyapps.vividpath.data.model.PathStatus
import com.dailyapps.vividpath.data.model.Reflection
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VividRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val pathDao = db.pathDao()
    private val intentionDao = db.intentionDao()
    private val focusDao = db.focusDao()
    private val reflectionDao = db.reflectionDao()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun todayKey(): String = LocalDate.now().format(dateFormatter)

    fun getPathItems(dayKey: String = todayKey()): Flow<List<PathItem>> =
        pathDao.getItemsForDay(dayKey)

    fun getPendingItems(dayKey: String = todayKey()): Flow<List<PathItem>> =
        pathDao.getPendingItems(dayKey)

    suspend fun addPathItem(title: String, notes: String = "", priority: com.dailyapps.vividpath.data.model.PathPriority = com.dailyapps.vividpath.data.model.PathPriority.MEDIUM, estimatedMinutes: Int = 25) {
        pathDao.insertItem(
            PathItem(
                title = title.trim(),
                notes = notes.trim(),
                priority = priority,
                estimatedMinutes = estimatedMinutes,
                dayKey = todayKey()
            )
        )
    }

    suspend fun updatePathItem(item: PathItem) = pathDao.updateItem(item)

    suspend fun completePathItem(item: PathItem) {
        pathDao.updateItem(
            item.copy(
                status = PathStatus.DONE,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deletePathItem(id: Long) = pathDao.deleteItem(id)

    fun countCompleted(dayKey: String = todayKey()) = pathDao.countCompleted(dayKey)
    fun countTotal(dayKey: String = todayKey()) = pathDao.countTotal(dayKey)

    fun getIntention(dayKey: String = todayKey()): Flow<DailyIntention?> =
        intentionDao.getIntention(dayKey)

    suspend fun setIntention(text: String) {
        intentionDao.upsert(
            DailyIntention(
                dayKey = todayKey(),
                intention = text.trim()
            )
        )
    }

    fun getFocusSessions(dayKey: String = todayKey()) = focusDao.getSessionsForDay(dayKey)

    suspend fun startFocusSession(durationMinutes: Int, pathItemId: Long? = null): Long {
        return focusDao.insertSession(
            FocusSession(
                pathItemId = pathItemId,
                durationMinutes = durationMinutes,
                dayKey = todayKey()
            )
        )
    }

    suspend fun completeFocusSession(session: FocusSession) {
        focusDao.updateSession(
            session.copy(
                completed = true,
                endedAt = System.currentTimeMillis()
            )
        )
    }

    fun totalFocusMinutes(dayKey: String = todayKey()) = focusDao.totalFocusMinutes(dayKey)

    fun getReflection(dayKey: String = todayKey()) = reflectionDao.getReflection(dayKey)

    suspend fun saveReflection(
        mood: Int,
        energy: Int,
        wins: String,
        lessons: String,
        gratitude: String
    ) {
        reflectionDao.upsert(
            Reflection(
                dayKey = todayKey(),
                mood = mood.coerceIn(1, 5),
                energy = energy.coerceIn(1, 5),
                wins = wins.trim(),
                lessons = lessons.trim(),
                gratitude = gratitude.trim()
            )
        )
    }
}
