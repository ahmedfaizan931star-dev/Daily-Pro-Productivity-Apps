package com.dailyapps.luminafocus.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.luminafocus.ui.components.EmptyState
import com.dailyapps.luminafocus.ui.components.LevelDots
import com.dailyapps.luminafocus.ui.components.SectionHeader
import com.dailyapps.luminafocus.ui.theme.EnergyHigh
import com.dailyapps.luminafocus.ui.theme.EnergyLow
import com.dailyapps.luminafocus.ui.theme.EnergyMed
import com.dailyapps.luminafocus.ui.theme.MoodLow
import com.dailyapps.luminafocus.ui.theme.MoodNeutral
import com.dailyapps.luminafocus.ui.theme.MoodPositive
import com.dailyapps.luminafocus.viewmodel.LuminaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InsightsScreen(viewModel: LuminaViewModel) {
    val state by viewModel.uiState.collectAsState()
    var energy by remember { mutableFloatStateOf(3f) }
    var mood by remember { mutableFloatStateOf(3f) }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Log energy & mood to understand your patterns.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))
        SectionHeader("Log now")

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Energy: ${energy.toInt()}", style = MaterialTheme.typography.titleMedium)
                LevelDots(
                    level = energy.toInt(),
                    activeColor = when {
                        energy >= 4f -> EnergyHigh
                        energy >= 2.5f -> EnergyMed
                        else -> EnergyLow
                    }
                )
                Slider(
                    value = energy,
                    onValueChange = { energy = it },
                    valueRange = 1f..5f,
                    steps = 3
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Mood: ${mood.toInt()}", style = MaterialTheme.typography.titleMedium)
                LevelDots(
                    level = mood.toInt(),
                    activeColor = when {
                        mood >= 4f -> MoodPositive
                        mood >= 2.5f -> MoodNeutral
                        else -> MoodLow
                    }
                )
                Slider(
                    value = mood,
                    onValueChange = { mood = it },
                    valueRange = 1f..5f,
                    steps = 3
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Optional note") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.logEnergy(energy.toInt(), mood.toInt(), note.trim())
                        note = ""
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save log")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader("Recent logs")

        if (state.energyLogs.isEmpty()) {
            EmptyState("No energy/mood logs yet.")
        } else {
            state.energyLogs.take(10).forEach { log ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Energy ${log.energyLevel} · Mood ${log.mood}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (log.note.isNotBlank()) {
                                Text(
                                    text = log.note,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Text(
                            text = SimpleDateFormat("MMM d HH:mm", Locale.getDefault()).format(Date(log.loggedAt)),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
