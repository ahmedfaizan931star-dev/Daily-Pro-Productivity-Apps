package com.dailyapps.harborrite.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.harborrite.viewmodel.HarborViewModel

@Composable
fun RitesScreen(vm: HarborViewModel) {
    val state by vm.state.collectAsState()
    var text by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(state.berths.firstOrNull()?.id ?: 0L) }
    if (selected == 0L && state.berths.isNotEmpty()) selected = state.berths.first().id

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Weekly rites", style = MaterialTheme.typography.headlineSmall)
        if (state.berths.isEmpty()) {
            Text("Create a berth first.")
            return
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            state.berths.take(4).forEach { berth ->
                FilterChip(
                    selected = selected == berth.id,
                    onClick = { selected = berth.id },
                    label = { Text(berth.name) }
                )
            }
        }
        OutlinedTextField(
            text,
            { text = it },
            label = { Text("What will you leave at the dock?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Button(onClick = {
            vm.addRite(selected, text)
            text = ""
        }, enabled = selected != 0L) { Text("Save rite") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.rites, key = { it.id }) { item ->
                val name = state.berths.find { it.id == item.berthId }?.name ?: "Berth"
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                        Column(Modifier.weight(1f)) {
                            Text(name, style = MaterialTheme.typography.titleMedium)
                            Text(item.reflection)
                        }
                        IconButton(onClick = { vm.deleteRite(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
