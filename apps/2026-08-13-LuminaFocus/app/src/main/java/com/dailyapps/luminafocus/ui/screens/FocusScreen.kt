package com.dailyapps.luminafocus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.dailyapps.luminafocus.ui.components.formatTime
import com.dailyapps.luminafocus.viewmodel.LuminaViewModel

@Composable
fun FocusScreen(viewModel: LuminaViewModel) {
    val state by viewModel.uiState.collectAsState()
    val timer = state.timer

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = timer.mode.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(timer.remainingSeconds),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(
                Triple(25, "Pomodoro", "pomodoro"),
                Triple(50, "Deep Work", "deep"),
                Triple(15, "Short", "custom")
            ).forEach { (mins, label, mode) ->
                FilterChip(
                    selected = timer.totalSeconds == mins * 60 && !timer.isRunning,
                    onClick = { if (!timer.isRunning) viewModel.setTimerPreset(mins, mode) },
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            IconButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(28.dp))
            }

            Button(
                onClick = { viewModel.startOrPauseTimer() },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (timer.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (timer.isRunning) "Pause" else "Start",
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.size(56.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Today: ${state.todayMinutes} min · ${state.todaySessions.size} sessions",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
