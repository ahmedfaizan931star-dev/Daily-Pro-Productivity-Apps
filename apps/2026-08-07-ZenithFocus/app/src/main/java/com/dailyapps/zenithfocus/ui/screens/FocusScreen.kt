package com.dailyapps.zenithfocus.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyapps.zenithfocus.viewmodel.ZenithViewModel

@Composable
fun FocusScreen(viewModel: ZenithViewModel) {
    val timer by viewModel.timerState.collectAsState()
    val minutesToday by viewModel.focusMinutesToday.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Today: ${minutesToday} minutes of deep work",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mode selector
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "Pomodoro" to 25,
                "Deep Work" to 50,
                "Flow" to 90
            ).forEach { (name, mins) ->
                FilterChip(
                    selected = timer.mode == name,
                    onClick = { viewModel.setTimerMode(name, mins) },
                    label = { Text("$name ${mins}m") },
                    enabled = !timer.isRunning
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Circular progress
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(260.dp)
        ) {
            val primary = MaterialTheme.colorScheme.primary
            val track = MaterialTheme.colorScheme.surfaceVariant
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 18.dp.toPx()
                drawArc(
                    color = track,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = primary,
                    startAngle = -90f,
                    sweepAngle = 360f * timer.progress,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(timer.remainingSeconds),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timer.mode,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(onClick = { viewModel.resetTimer() }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Reset")
            }

            Button(
                onClick = {
                    if (timer.isRunning) viewModel.pauseTimer()
                    else viewModel.startTimer()
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    if (timer.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(if (timer.isRunning) "Pause" else if (timer.isPaused) "Resume" else "Start")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tip: Match high-energy periods with Do-First tasks for peak performance.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%02d:%02d".format(m, s)
}
