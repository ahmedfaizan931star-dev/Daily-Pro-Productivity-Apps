package com.dailyapps.harborrite.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.harborrite.viewmodel.HarborViewModel

@Composable
fun HarborScreen(vm: HarborViewModel) {
    val state by vm.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("HarborRite", style = MaterialTheme.typography.headlineMedium)
        Text("Dock work into berths. Sail a voyage. Close the week with a rite.", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Week at sea", style = MaterialTheme.typography.labelMedium)
                    Text("${state.weeklyMinutes} min", style = MaterialTheme.typography.headlineSmall)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Berths", style = MaterialTheme.typography.labelMedium)
                    Text("${state.berths.size}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Busiest berth", style = MaterialTheme.typography.titleMedium)
                val busy = state.busiest
                if (busy == null) {
                    Text("Add a berth and log a voyage.")
                } else {
                    Text(busy.name, style = MaterialTheme.typography.headlineSmall)
                    Text(busy.intent.ifBlank { "No intent set" })
                    Text("Load ${state.loadFor(busy.id)}")
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Recent voyages", style = MaterialTheme.typography.titleMedium)
                if (state.voyages.isEmpty()) {
                    Text("No voyages yet.")
                } else {
                    state.voyages.take(5).forEach { s ->
                        val name = state.berths.find { it.id == s.berthId }?.name ?: "Berth"
                        Text("$name · ${s.minutes}m · tide ${s.tide}")
                    }
                }
            }
        }
    }
}
