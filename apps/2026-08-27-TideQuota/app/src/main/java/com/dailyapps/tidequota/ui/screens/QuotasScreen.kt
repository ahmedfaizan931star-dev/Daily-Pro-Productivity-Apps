package com.dailyapps.tidequota.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.tidequota.data.model.LifeDomain
import com.dailyapps.tidequota.viewmodel.TideUiState
import java.util.Locale

@Composable
fun QuotasScreen(
    state: TideUiState,
    onSet: (LifeDomain, Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Weekly quotas", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Cap hours per domain so the week cannot silently overflow.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        Text(
            String.format(Locale.US, "Total planned · %.0f hours", state.plannedTotal),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LifeDomain.entries.forEach { domain ->
            val hours = state.quotas.firstOrNull { it.domain == domain.name }?.weeklyHours
                ?: domain.defaultHours
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "${domain.label}  · ${hours.toInt()}h",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Slider(
                        value = hours,
                        onValueChange = { onSet(domain, it) },
                        valueRange = 0f..40f,
                        steps = 39
                    )
                }
            }
        }
    }
}
