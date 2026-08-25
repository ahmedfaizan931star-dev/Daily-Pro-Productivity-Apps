package com.dailyapps.astraforge.ui.screens

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
import com.dailyapps.astraforge.viewmodel.AstraViewModel

@Composable
fun FocusScreen(viewModel: AstraViewModel) {
    val timer by viewModel.timer.collectAsState()
    val minutes = timer.remainingSeconds / 60
    val seconds = timer.remainingSeconds % 60
    val progress = if (timer.totalSeconds > 0) {
        1f - (timer.remainingSeconds.toFloat() / timer.totalSeconds)
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Deep Work",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (timer.isRunning) "In session…" else "Ready",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(15, 25, 50).forEach { m ->
                FilterChip(
                    selected = timer.totalSeconds == m * 60 && !timer.isRunning,
                    onClick = { if (!timer.isRunning) viewModel.setTimerMinutes(m) },
                    label = { Text("${m}m") }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (timer.isRunning) {
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

        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Progress: ${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
