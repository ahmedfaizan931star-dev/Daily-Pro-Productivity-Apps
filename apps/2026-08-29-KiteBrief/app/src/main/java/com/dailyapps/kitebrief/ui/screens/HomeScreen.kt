package com.dailyapps.kitebrief.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.kitebrief.data.model.Brief
import com.dailyapps.kitebrief.data.model.Commitment

@Composable
fun HomeScreen(
    brief: Brief?,
    commitments: List<Commitment>,
    onSaveIntention: (String, Int) -> Unit,
    onOpenCommitments: () -> Unit,
    onOpenShutdown: () -> Unit,
    onOpenWind: () -> Unit
) {
    var intention by remember(brief?.intention) { mutableStateOf(brief?.intention.orEmpty()) }
    var energy by remember(brief?.energy) { mutableFloatStateOf((brief?.energy ?: 3).toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("KiteBrief", style = MaterialTheme.typography.headlineLarge)
        Text("Lift the day with one clear line of wind.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(20.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.padding(16.dp)) {
                Text("Morning briefing", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = intention,
                    onValueChange = { intention = it },
                    label = { Text("Today's intention") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text("Energy ${energy.toInt()}/5")
                Slider(value = energy, onValueChange = { energy = it }, valueRange = 1f..5f, steps = 3)
                Button(onClick = { onSaveIntention(intention, energy.toInt()) }) { Text("Set briefing") }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Commitments", style = MaterialTheme.typography.titleLarge)
                Text("${commitments.count { it.done }}/${commitments.size} flying")
                TextButton(onClick = onOpenCommitments) { Text("Open commitments") }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onOpenShutdown, modifier = Modifier.weight(1f)) { Text("Land the day") }
            Button(onClick = onOpenWind, modifier = Modifier.weight(1f)) { Text("Wind map") }
        }
    }
}
