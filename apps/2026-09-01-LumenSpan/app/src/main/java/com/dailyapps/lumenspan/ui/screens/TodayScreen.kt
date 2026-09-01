package com.dailyapps.lumenspan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyapps.lumenspan.data.EnergyBand
import com.dailyapps.lumenspan.data.SpanTask
import com.dailyapps.lumenspan.viewmodel.LumenViewModel

@Composable
fun TodayScreen(vm: LumenViewModel) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("45") }
    var band by remember { mutableStateOf(EnergyBand.PEAK) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("LumenSpan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Schedule work to the energy you actually have.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Work block") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = minutes,
            onValueChange = { minutes = it.filter(Char::isDigit).take(3) },
            label = { Text("Minutes") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EnergyBand.entries.forEach { option ->
                FilterChip(
                    selected = band == option,
                    onClick = { band = option },
                    label = { Text(option.name.lowercase().replaceFirstChar { it.titlecase() }) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                vm.addTask(title, band, minutes.toIntOrNull() ?: 45)
                title = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add to today's span") }
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            EnergyBand.entries.forEach { group ->
                val items = state.tasks.filter { it.band == group }
                if (items.isNotEmpty()) {
                    item {
                        Text(
                            group.name.lowercase().replaceFirstChar { it.titlecase() },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    items(items, key = { it.id }) { task ->
                        TaskRow(task, onToggle = { vm.toggle(task) }, onDelete = { vm.remove(task) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskRow(task: SpanTask, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.done, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, fontWeight = FontWeight.Medium)
                Text("${task.minutes} min · ${task.band.name.lowercase()}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
            }
        }
    }
}
