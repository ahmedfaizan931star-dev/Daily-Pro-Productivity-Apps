package com.dailyapps.nexusflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyapps.nexusflow.viewmodel.NexusViewModel

@Composable
fun FocusScreen(viewModel: NexusViewModel) {
    val state by viewModel.uiState.collectAsState()
    val minutes = state.remainingSeconds / 60
    val seconds = state.remainingSeconds % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Today: ${state.todayFocusMinutes} min",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(15, 25, 45, 60).forEach { min ->
                FilterChip(
                    selected = state.selectedDuration == min,
                    onClick = { viewModel.setDuration(min) },
                    label = { Text("${min}m") },
                    enabled = !state.isTimerRunning
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isTimerRunning) {
                Button(onClick = { viewModel.pauseTimer() }) {
                    Text("Pause")
                }
            } else {
                Button(onClick = { viewModel.startTimer() }) {
                    Text("Start")
                }
            }
            OutlinedButton(onClick = { viewModel.resetTimer() }) {
                Text("Reset")
            }
        }
    }
}
