package com.dailyapps.tidequota.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dailyapps.tidequota.data.local.AppDatabase
import com.dailyapps.tidequota.data.model.DomainProgress
import com.dailyapps.tidequota.data.model.LifeDomain
import com.dailyapps.tidequota.data.model.QuotaEntity
import com.dailyapps.tidequota.data.model.TimeBlockEntity
import com.dailyapps.tidequota.data.repository.TideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TideUiState(
    val weekStart: Long = 0L,
    val quotas: List<QuotaEntity> = emptyList(),
    val blocks: List<TimeBlockEntity> = emptyList(),
    val progress: List<DomainProgress> = emptyList(),
    val plannedTotal: Float = 0f,
    val loggedTotal: Float = 0f
)

class TideViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = TideRepository(AppDatabase.get(app).tideDao())
    private val weekStart = MutableStateFlow(TideRepository.currentWeekStart())

    val uiState: StateFlow<TideUiState> = combine(
        weekStart,
        repo.observeQuotas(),
        repo.observeBlocks(TideRepository.currentWeekStart())
    ) { start, quotas, blocks ->
        if (quotas.isEmpty()) {
            viewModelScope.launch { repo.seedIfNeeded(quotas) }
        }
        val progress = LifeDomain.entries.map { domain ->
            val planned = quotas.firstOrNull { it.domain == domain.name }?.weeklyHours
                ?: domain.defaultHours
            val logged = blocks.filter { it.domain == domain.name }
                .sumOf { it.minutes }.toFloat() / 60f
            DomainProgress(domain, planned, logged)
        }
        TideUiState(
            weekStart = start,
            quotas = quotas,
            blocks = blocks,
            progress = progress,
            plannedTotal = progress.sumOf { it.plannedHours.toDouble() }.toFloat(),
            loggedTotal = progress.sumOf { it.loggedHours.toDouble() }.toFloat()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TideUiState())

    fun setQuota(domain: LifeDomain, hours: Float) {
        viewModelScope.launch { repo.setQuota(domain, hours) }
    }

    fun log(domain: LifeDomain, minutes: Int, note: String) {
        viewModelScope.launch {
            repo.logBlock(domain, minutes, note, weekStart.value)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repo.deleteBlock(id) }
    }

    companion object {
        fun factory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TideViewModel(app) as T
                }
            }
    }
}
