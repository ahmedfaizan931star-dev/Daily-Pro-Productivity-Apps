package com.dailyapps.amberkiln.ui.screens

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
import com.dailyapps.amberkiln.viewmodel.AmberViewModel

@Composable
fun ForgeScreen(vm: AmberViewModel) {
    val state by vm.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("AmberKiln", style = MaterialTheme.typography.headlineMedium)
        Text("Keep one kiln hot. Log fire, then cool down.", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Week fire", style = MaterialTheme.typography.labelMedium)
                    Text("${state.weeklyMinutes} min", style = MaterialTheme.typography.headlineSmall)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Kilns", style = MaterialTheme.typography.labelMedium)
                    Text("${state.kilns.size}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hottest kiln", style = MaterialTheme.typography.titleMedium)
                val hot = state.hottest
                if (hot == null) {
                    Text("Add a kiln and log a fire session.")
                } else {
                    Text(hot.name, style = MaterialTheme.typography.headlineSmall)
                    Text(hot.intent.ifBlank { "No intent set" })
                    Text("Heat ${state.heatFor(hot.id)}")
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Recent fire", style = MaterialTheme.typography.titleMedium)
                if (state.sessions.isEmpty()) {
                    Text("No sessions yet.")
                } else {
                    state.sessions.take(5).forEach { s ->
                        val name = state.kilns.find { it.id == s.kilnId }?.name ?: "Kiln"
                        Text("$name · ${s.minutes}m · intensity ${s.intensity}")
                    }
                }
            }
        }
    }
}
