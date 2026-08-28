package com.dailyapps.stillpoint.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.stillpoint.viewmodel.StillUiState
import com.dailyapps.stillpoint.viewmodel.StillViewModel

@Composable
fun QuietScreen(state: StillUiState, vm: StillViewModel) {
    var title by remember { mutableStateOf("") }
    var start by remember { mutableFloatStateOf(9f) }
    var duration by remember { mutableFloatStateOf(60f) }
    var intentText by remember { mutableStateOf("") }
    var energy by remember { mutableIntStateOf(3) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Quiet hours", fontWeight = FontWeight.SemiBold)
            Text("Schedule sanctuary blocks and pin the one thing that matters today.")
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Block name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Starts %02d:00".format(start.toInt()))
                    Slider(value = start, onValueChange = { start = it }, valueRange = 6f..22f, steps = 15)
                    Text("${duration.toInt()} minutes")
                    Slider(value = duration, onValueChange = { duration = it }, valueRange = 25f..180f, steps = 6)
                    Button(onClick = {
                        vm.addBlock(title, start.toInt(), duration.toInt())
                        title = ""
                    }) { Text("Protect this window") }
                }
            }
        }
        items(state.blocks, key = { it.id }) { block ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(block.title, fontWeight = FontWeight.Medium)
                    Text("%02d:00 · ${block.durationMin} min".format(block.startHour))
                    Row {
                        TextButton(onClick = { vm.toggleBlock(block) }) {
                            Text(if (block.enabled) "Disable" else "Enable")
                        }
                        TextButton(onClick = { vm.deleteBlock(block) }) { Text("Remove") }
                    }
                }
            }
        }
        item {
            Spacer(Modifier.height(8.dp))
            Text("Intentions", fontWeight = FontWeight.SemiBold)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = intentText,
                        onValueChange = { intentText = it },
                        label = { Text("One sentence intention") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Energy needed")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { n ->
                            FilterChip(
                                selected = energy == n,
                                onClick = { energy = n },
                                label = { Text("$n") }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        vm.addIntention(intentText, energy)
                        intentText = ""
                    }) { Text("Anchor intention") }
                }
            }
        }
        items(state.intentions, key = { it.id }) { item ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(item.text, fontWeight = FontWeight.Medium)
                    Text(if (item.completed) "Closed" else "Open · energy ${item.energy}")
                    Row {
                        TextButton(onClick = { vm.toggleIntention(item) }) {
                            Text(if (item.completed) "Reopen" else "Complete")
                        }
                        TextButton(onClick = { vm.deleteIntention(item) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}
