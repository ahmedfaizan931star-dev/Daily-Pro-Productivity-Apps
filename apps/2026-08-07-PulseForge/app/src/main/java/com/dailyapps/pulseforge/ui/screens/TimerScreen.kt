package com.dailyapps.pulseforge.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.pulseforge.data.model.FocusMode
import com.dailyapps.pulseforge.ui.theme.PulseEmerald
import com.dailyapps.pulseforge.ui.theme.PulseIndigo
import com.dailyapps.pulseforge.viewmodel.PulseViewModel

@Composable
fun TimerScreen(viewModel: PulseViewModel) {
    val state by viewModel.timer.collectAsState()
    val progress by animateFloatAsState(
        targetValue = if (state.totalSeconds > 0)
            state.remainingSeconds.toFloat() / state.totalSeconds
        else 0f,
        label = "timerProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (state.isBreak) "Break Time" else "Focus Pulse",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = state.selectedMode.label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(260.dp)
        ) {
            val trackColor = MaterialTheme.colorScheme.surfaceVariant
            val progressColor = if (state.isBreak) PulseEmerald else PulseIndigo
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 18.dp.toPx()
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    size = Size(size.width - stroke, size.height - stroke),
                    topLeft = Offset(stroke / 2, stroke / 2)
                )
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    size = Size(size.width - stroke, size.height - stroke),
                    topLeft = Offset(stroke / 2, stroke / 2)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(state.remainingSeconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (state.isRunning) "In progress" else "Ready",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = { viewModel.resetTimer() },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset")
            }

            FilledIconButton(
                onClick = {
                    if (state.isRunning) viewModel.pauseTimer()
                    else viewModel.startTimer()
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isRunning) "Pause" else "Start",
                    modifier = Modifier.size(36.dp)
                )
            }

            FilledIconButton(
                onClick = { viewModel.skipToBreakOrWork() },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Skip")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Session Mode",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FocusMode.entries.filter { it != FocusMode.CUSTOM }.forEach { mode ->
                FilterChip(
                    selected = state.selectedMode == mode,
                    onClick = { viewModel.selectMode(mode) },
                    label = { Text(mode.label.split(" ").first()) },
                    enabled = !state.isRunning,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            FilterChip(
                selected = state.selectedMode == FocusMode.CUSTOM,
                onClick = { viewModel.selectMode(FocusMode.CUSTOM) },
                label = { Text("Custom") },
                enabled = !state.isRunning,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }

        if (state.selectedMode == FocusMode.CUSTOM) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Work: ${state.customWorkMinutes} min",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = state.customWorkMinutes.toFloat(),
                onValueChange = {
                    viewModel.updateCustomMinutes(it.toInt(), state.customBreakMinutes)
                },
                valueRange = 5f..90f,
                steps = 16,
                enabled = !state.isRunning
            )
            Text(
                text = "Break: ${state.customBreakMinutes} min",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = state.customBreakMinutes.toFloat(),
                onValueChange = {
                    viewModel.updateCustomMinutes(state.customWorkMinutes, it.toInt())
                },
                valueRange = 1f..20f,
                steps = 18,
                enabled = !state.isRunning
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sessions completed this run: ${state.sessionsCompletedToday}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}

private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%02d:%02d".format(m, s)
}
