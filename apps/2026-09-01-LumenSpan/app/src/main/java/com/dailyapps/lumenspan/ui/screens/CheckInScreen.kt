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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyapps.lumenspan.viewmodel.LumenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckInScreen(vm: LumenViewModel) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var score by remember { mutableIntStateOf(3) }
    var note by remember { mutableStateOf("") }
    val fmt = remember { SimpleDateFormat("MMM d · HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Energy check-in", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Log how lit you feel before the next block.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { value ->
                FilterChip(
                    selected = score == value,
                    onClick = { score = value },
                    label = { Text("$value") }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("What is draining or charging you?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                vm.checkIn(score, note)
                note = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save check-in") }
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.checkins, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Energy ${item.score}/5", fontWeight = FontWeight.SemiBold)
                        if (item.note.isNotBlank()) Text(item.note)
                        Text(fmt.format(Date(item.timestamp)), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
