package com.dailyapps.tidequota.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.tidequota.viewmodel.TideUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HarborScreen(state: TideUiState) {
    val weekLabel = SimpleDateFormat("MMM d", Locale.getDefault())
        .format(Date(state.weekStart))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("TideQuota", style = MaterialTheme.typography.headlineLarge)
        Text("Week of $weekLabel", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("This week", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Stat("Logged", String.format(Locale.US, "%.1fh", state.loggedTotal))
                    Stat("Planned", String.format(Locale.US, "%.1fh", state.plannedTotal))
                    val remain = (state.plannedTotal - state.loggedTotal).coerceAtLeast(0f)
                    Stat("Left", String.format(Locale.US, "%.1fh", remain))
                }
                Spacer(Modifier.height(12.dp))
                val overall = if (state.plannedTotal <= 0f) 0f else
                    (state.loggedTotal / state.plannedTotal).coerceIn(0f, 1f)
                LinearProgressIndicator(progress = { overall }, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Domain tides", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        state.progress.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.domain.label, fontWeight = FontWeight.SemiBold)
                        Text(
                            String.format(
                                Locale.US,
                                "%.1f / %.0fh",
                                item.loggedHours,
                                item.plannedHours
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { item.ratio.coerceAtMost(1f) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (item.overspend > 0f) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            String.format(Locale.US, "Over by %.1fh", item.overspend),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.titleLarge)
    }
}
