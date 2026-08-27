package com.dailyapps.tidequota.data.repository

import com.dailyapps.tidequota.data.local.TideDao
import com.dailyapps.tidequota.data.model.LifeDomain
import com.dailyapps.tidequota.data.model.QuotaEntity
import com.dailyapps.tidequota.data.model.TimeBlockEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TideRepository(private val dao: TideDao) {

    fun observeQuotas(): Flow<List<QuotaEntity>> = dao.observeQuotas()

    fun observeBlocks(weekStart: Long): Flow<List<TimeBlockEntity>> =
        dao.observeBlocks(weekStart)

    suspend fun seedIfNeeded(existing: List<QuotaEntity>) {
        if (existing.isNotEmpty()) return
        LifeDomain.entries.forEach { domain ->
            dao.upsertQuota(QuotaEntity(domain.name, domain.defaultHours))
        }
    }

    suspend fun setQuota(domain: LifeDomain, hours: Float) {
        dao.upsertQuota(QuotaEntity(domain.name, hours.coerceIn(0f, 80f)))
    }

    suspend fun logBlock(domain: LifeDomain, minutes: Int, note: String, weekStart: Long) {
        dao.insertBlock(
            TimeBlockEntity(
                domain = domain.name,
                minutes = minutes.coerceAtLeast(5),
                note = note.trim(),
                createdAt = System.currentTimeMillis(),
                weekStart = weekStart
            )
        )
    }

    suspend fun deleteBlock(id: Long) = dao.deleteBlock(id)

    companion object {
        fun currentWeekStart(): Long {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val dow = cal.get(Calendar.DAY_OF_WEEK)
            val diff = if (dow == Calendar.SUNDAY) -6 else Calendar.MONDAY - dow
            cal.add(Calendar.DAY_OF_MONTH, diff)
            return cal.timeInMillis
        }
    }
}
