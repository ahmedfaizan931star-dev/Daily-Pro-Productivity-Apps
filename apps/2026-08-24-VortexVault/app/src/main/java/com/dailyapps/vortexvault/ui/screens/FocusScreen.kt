package com.dailyapps.vortexvault.ui.screens

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
import com.dailyapps.vortexvault.viewmodel.VortexViewModel

@Composable
fun FocusScreen(viewModel: VortexViewModel) {
    val timer by viewModel.timer.collectAsState()
    val todayMinutes by viewModel.todayFocusMinutes.collectAsState()

    val minutes = timer.remainingSeconds / 60
    val seconds = timer.remainingSeconds % 60
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
            text = "Today: $todayMinutes min",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(15, 25, 45, 50).forEach { min ->
                FilterChip(
                    selected = timer.totalSeconds == min * 60 && !timer.isRunning,
                    onClick = { viewModel.setTimerDuration(min) },
                    label = { Text("${min}m") },
                    enabled = !timer.isRunning
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (timer.isRunning) {
                OutlinedButton(
                    onClick = { viewModel.pauseTimer() },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp)
                ) {
                    Text("Pause")
                }
            } else {
                Button(
                    onClick = { viewModel.startTimer() },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp)
                ) {
                    Text("Start")
                }
            }
            OutlinedButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier.size(width = 120.dp, height = 48.dp)
            ) {
                Text("Reset")
            }
        }
    }
}
