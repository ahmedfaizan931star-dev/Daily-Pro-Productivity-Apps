package com.dailyapps.momentumvault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.momentumvault.ui.components.SectionHeader
import com.dailyapps.momentumvault.viewmodel.MomentumViewModel

@Composable
fun ReflectScreen(viewModel: MomentumViewModel) {
    val state by viewModel.uiState.collectAsState()
    val existing = state.todayEnergy

    var energy by remember(existing) { mutableIntStateOf(existing?.energyLevel ?: 3) }
    var mood by remember(existing) { mutableIntStateOf(existing?.mood ?: 3) }
    var journal by remember(existing) { mutableStateOf(existing?.journal ?: "") }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Reflect",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Close the day with energy, mood and a short note.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("Energy (1–5)")
        LevelSelector(selected = energy, onSelect = { energy = it })

        Spacer(modifier = Modifier.height(20.dp))

        SectionHeader("Mood (1–5)")
        LevelSelector(selected = mood, onSelect = { mood = it })

        Spacer(modifier = Modifier.height(20.dp))

        SectionHeader("Journal")
        OutlinedTextField(
            value = journal,
            onValueChange = { journal = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = { Text("What moved the needle today?") },
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.saveEnergy(energy, mood, journal)
                saved = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (existing != null || saved) "Update Reflection" else "Save Reflection")
        }

        if (existing != null || saved) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "Reflection saved for today. +20 momentum points unlocked.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun LevelSelector(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        (1..5).forEach { level ->
            val isSelected = selected == level
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onSelect(level) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$level",
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
