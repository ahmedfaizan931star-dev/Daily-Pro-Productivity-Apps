package com.dailyapps.stillpoint.ui.screens

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.stillpoint.viewmodel.StillUiState
import com.dailyapps.stillpoint.viewmodel.StillViewModel
import java.util.Calendar

@Composable
fun HomeScreen(state: StillUiState, vm: StillViewModel) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    val open = state.intentions.count { !it.completed }
    val done = state.intentions.count { it.completed }
    val weekMinutes = state.sessions.take(14).sumOf { it.minutes }
    val activeBlocks = state.blocks.count { it.enabled }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(greeting, style = MaterialTheme.typography.titleMedium)
        Text(
            "Stillpoint",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Protect quiet hours. Keep one intention. Log the work that actually happened.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Quiet blocks", "$activeBlocks live", Modifier.weight(1f))
            StatCard("Open intents", "$open", Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Completed", "$done", Modifier.weight(1f))
            StatCard("Focus logged", "${weekMinutes}m", Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))
        Text("Today's intention", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        val current = state.intentions.firstOrNull { !it.completed }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                if (current == null) {
                    Text("No open intention. Add one on the Quiet tab.")
                } else {
                    Text(current.text, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Energy ${current.energy}/5", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { current.energy / 5f },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Protected windows", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        if (state.blocks.isEmpty()) {
            Text("Create weekday quiet hours so notifications wait outside the sanctuary.")
        } else {
            state.blocks.take(4).forEach { block ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(block.title, fontWeight = FontWeight.Medium)
                            Text(
                                "%02d:00 · %d min".format(block.startHour, block.durationMin),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(if (block.enabled) "On" else "Off")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
