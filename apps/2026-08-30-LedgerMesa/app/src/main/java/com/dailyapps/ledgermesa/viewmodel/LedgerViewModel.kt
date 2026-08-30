package com.dailyapps.ledgermesa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.ledgermesa.data.model.Envelope
import com.dailyapps.ledgermesa.data.model.Spend
import com.dailyapps.ledgermesa.data.repository.LedgerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class EnvelopeStatus(
    val envelope: Envelope,
    val spentCents: Int
) {
    val remainingCents: Int get() = envelope.weeklyLimitCents - spentCents
    val usedPct: Float get() =
        if (envelope.weeklyLimitCents <= 0) 0f
        else (spentCents.toFloat() / envelope.weeklyLimitCents).coerceIn(0f, 1.4f)
}

data class LedgerUi(
    val envelopes: List<EnvelopeStatus> = emptyList(),
    val recent: List<Spend> = emptyList(),
    val weekSpentCents: Int = 0,
    val weekLimitCents: Int = 0
) {
    val remainingCents: Int get() = weekLimitCents - weekSpentCents
}

class LedgerViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = LedgerRepository(app)

    val state: StateFlow<LedgerUi> = combine(repo.envelopes, repo.spends) { env, spends ->
        val weekStart = startOfWeek()
        val weekSpends = spends.filter { it.createdAt >= weekStart }
        val statuses = env.map { e ->
            EnvelopeStatus(e, weekSpends.filter { it.envelopeId == e.id }.sumOf { it.amountCents })
        }
        LedgerUi(
            envelopes = statuses,
            recent = spends.take(20),
            weekSpentCents = weekSpends.sumOf { it.amountCents },
            weekLimitCents = env.sumOf { it.weeklyLimitCents }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LedgerUi())

    fun addEnvelope(name: String, dollars: Int) = viewModelScope.launch {
        if (name.isBlank() || dollars <= 0) return@launch
        repo.addEnvelope(name.trim(), dollars * 100)
    }

    fun addSpend(envelopeId: Long, dollars: Int, note: String) = viewModelScope.launch {
        if (envelopeId == 0L || dollars <= 0) return@launch
        repo.addSpend(envelopeId, dollars * 100, note.trim())
    }

    private fun startOfWeek(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        return cal.timeInMillis
    }
}
