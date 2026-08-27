package com.dailyapps.tidequota.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.tidequota.data.model.LifeDomain
import com.dailyapps.tidequota.viewmodel.TideUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LogScreen(
    state: TideUiState,
    onLog: (LifeDomain, Int, String) -> Unit,
    onDelete: (Long) -> Unit
) {
    var domain by remember { mutableStateOf(LifeDomain.DEEP_WORK) }
    var minutes by remember { mutableIntStateOf(30) }
    var note by remember { mutableStateOf("") }
    val stamp = SimpleDateFormat("EEE HH:mm", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Log a block", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(15, 30, 45, 60, 90).forEach { m ->
                    FilterChip(
                        selected = minutes == m,
                        onClick = { minutes = m },
                        label = { Text("${m}m") }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Column {
                LifeDomain.entries.chunked(3).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { d ->
                            FilterChip(
                                selected = domain == d,
                                onClick = { domain = d },
                                label = { Text(d.label) }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("What did you do?") }
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    onLog(domain, minutes, note)
                    note = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save block") }
            Spacer(Modifier.height(12.dp))
            Text("This week", style = MaterialTheme.typography.titleLarge)
        }
        items(state.blocks, key = { it.id }) { block ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${block.domain.replace('_', ' ')} · ${block.minutes}m",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(onClick = { onDelete(block.id) }) { Text("Remove") }
                    }
                    if (block.note.isNotBlank()) {
                        Text(block.note, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        stamp.format(Date(block.createdAt)),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
