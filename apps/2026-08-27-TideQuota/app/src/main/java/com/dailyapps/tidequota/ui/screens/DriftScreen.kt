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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.tidequota.viewmodel.TideUiState
import java.util.Locale

@Composable
fun DriftScreen(state: TideUiState) {
    val over = state.progress.filter { it.overspend > 0f }.sortedByDescending { it.overspend }
    val unused = state.progress.filter { it.remaining >= 1f }.sortedByDescending { it.remaining }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Week drift", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Where hours leaked and where quota is still waiting.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                val util = if (state.plannedTotal <= 0f) 0f else
                    (state.loggedTotal / state.plannedTotal * 100f)
                Text("Utilization", style = MaterialTheme.typography.titleMedium)
                Text(
                    String.format(Locale.US, "%.0f%% of planned hours logged", util),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Overspend", style = MaterialTheme.typography.titleLarge)
        if (over.isEmpty()) {
            Text("No domain is over quota yet.", style = MaterialTheme.typography.bodyMedium)
        } else {
            over.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(item.domain.label, style = MaterialTheme.typography.titleMedium)
                        Text(
                            String.format(Locale.US, "+%.1f hours past the cap", item.overspend),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Unused quota", style = MaterialTheme.typography.titleLarge)
        if (unused.isEmpty()) {
            Text("Every domain is spoken for.", style = MaterialTheme.typography.bodyMedium)
        } else {
            unused.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(item.domain.label, style = MaterialTheme.typography.titleMedium)
                        Text(
                            String.format(
                                Locale.US,
                                "%.1fh still available this week",
                                item.remaining
                            )
                        )
                    }
                }
            }
        }
    }
}
