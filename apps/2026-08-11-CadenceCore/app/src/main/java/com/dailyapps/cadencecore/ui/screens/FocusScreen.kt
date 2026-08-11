package com.dailyapps.cadencecore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyapps.cadencecore.ui.components.SectionHeader
import com.dailyapps.cadencecore.viewmodel.CadenceViewModel

@Composable
fun FocusScreen(viewModel: CadenceViewModel) {
    val state by viewModel.uiState.collectAsState()
    val minutes = state.timerSecondsLeft / 60
    val seconds = state.timerSecondsLeft % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionHeader(
            title = "Focus",
            subtitle = "Protect a block of deep work"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (state.isTimerRunning) "In session…" else "Ready",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(15, 25, 45, 60).forEach { m ->
                FilterChip(
                    selected = state.selectedDurationMinutes == m,
                    onClick = { viewModel.setDuration(m) },
                    label = { Text("${m}m") },
                    enabled = !state.isTimerRunning
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(28.dp))
            }

            Button(
                onClick = {
                    if (state.isTimerRunning) viewModel.pauseTimer()
                    else viewModel.startTimer()
                },
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = if (state.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isTimerRunning) "Pause" else "Start",
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { viewModel.completeSessionEarly() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Stop, contentDescription = "Finish early", modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Today: ${state.focusMinutesToday} minutes across ${state.sessionsToday.size} sessions",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        if (state.sessionsToday.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recent sessions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            state.sessionsToday.take(5).forEach { session ->
                Text(
                    text = "• ${session.durationMinutes} min",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            }
        }
    }
}
