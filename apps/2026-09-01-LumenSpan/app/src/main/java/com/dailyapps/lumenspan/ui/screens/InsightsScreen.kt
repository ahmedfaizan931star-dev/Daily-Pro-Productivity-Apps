package com.dailyapps.lumenspan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyapps.lumenspan.data.EnergyBand
import com.dailyapps.lumenspan.viewmodel.LumenViewModel

@Composable
fun InsightsScreen(vm: LumenViewModel) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val planned = state.tasks.sumOf { it.minutes }
    val done = state.tasks.filter { it.done }.sumOf { it.minutes }
    val ratio = if (planned == 0) 0f else done.toFloat() / planned.toFloat()
    val avgEnergy = state.checkins.take(12).map { it.score }.average().takeIf { !it.isNaN() } ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Span insights", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Protect Peak hours. Park shallow work in Recovery.")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Completed minutes", fontWeight = FontWeight.SemiBold)
                Text("$done / $planned")
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(progress = { ratio.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recent energy average", fontWeight = FontWeight.SemiBold)
                Text(String.format("%.1f / 5", avgEnergy))
                Text(
                    if (avgEnergy >= 4) "Peak window is open — ship the hard work."
                    else if (avgEnergy >= 2.5) "Steady state. Keep blocks short."
                    else "Recovery day. Cut scope, not standards."
                )
            }
        }
        EnergyBand.entries.forEach { band ->
            val group = state.tasks.filter { it.band == band }
            val doneMin = group.filter { it.done }.sumOf { it.minutes }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(band.name.lowercase().replaceFirstChar { it.titlecase() }, fontWeight = FontWeight.SemiBold)
                    Text("${group.size} blocks · ${doneMin} min finished")
                }
            }
        }
    }
}
