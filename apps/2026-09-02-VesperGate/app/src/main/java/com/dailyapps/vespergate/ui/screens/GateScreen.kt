package com.dailyapps.vespergate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.vespergate.ui.GateUiState

@Composable
fun GateScreen(
    state: GateUiState,
    onSaveNote: (String) -> Unit,
    onSaveIntention: (String) -> Unit,
    onToggleRitual: (Int) -> Unit,
    onScore: (Int) -> Unit,
    onSeal: () -> Unit
) {
    var note by remember(state.tonight.note) { mutableStateOf(state.tonight.note) }
    var intention by remember(state.tonight.intention) { mutableStateOf(state.tonight.intention) }
    val rituals = listOf("Shutdown inbox", "Park loops", "Write intention", "Leave the desk")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("VesperGate", style = MaterialTheme.typography.headlineMedium)
        Text(
            if (state.tonight.sealed) "Tonight is sealed. Rest." else "Close the day before the day closes you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )
        Text("Rituals", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            rituals.forEachIndexed { index, label ->
                val on = state.tonight.ritualsDone and (1 shl index) != 0
                FilterChip(
                    selected = on,
                    onClick = { onToggleRitual(index) },
                    label = { Text(label) }
                )
            }
        }
        Text("How cleanly did you leave work? ${state.tonight.score}/5")
        Slider(
            value = state.tonight.score.toFloat(),
            onValueChange = { onScore(it.toInt()) },
            valueRange = 0f..5f,
            steps = 4
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("What still hums in your head?") },
            minLines = 3
        )
        Button(onClick = { onSaveNote(note) }) { Text("Save note") }
        OutlinedTextField(
            value = intention,
            onValueChange = { intention = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Tomorrow's first move") }
        )
        Button(onClick = { onSaveIntention(intention) }) { Text("Lock intention") }
        Button(
            onClick = onSeal,
            enabled = !state.tonight.sealed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.tonight.sealed) "Sealed" else "Seal the gate")
        }
    }
}
