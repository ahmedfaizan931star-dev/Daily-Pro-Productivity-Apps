package com.dailyapps.solsticeflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyapps.solsticeflow.viewmodel.SolsticeViewModel
import kotlinx.coroutines.delay

@Composable
fun FocusScreen(viewModel: SolsticeViewModel) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.timerRunning) {
        while (state.timerRunning) {
            delay(1000)
            viewModel.tickTimer()
        }
    }

    val minutes = state.timerSecondsLeft / 60
    val seconds = state.timerSecondsLeft % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Focus session",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "25-minute deep work aligned with your energy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeText,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (state.timerRunning) "In progress" else "Ready",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!state.timerRunning) {
                Button(onClick = { viewModel.startTimer(25) }) {
                    Text("Start 25 min")
                }
            } else {
                OutlinedButton(onClick = { viewModel.pauseTimer() }) {
                    Text("Pause")
                }
                Button(onClick = { viewModel.completeTimer() }) {
                    Text("Complete")
                }
            }
            OutlinedButton(onClick = { viewModel.resetTimer() }) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Today: ${state.todayFocusMinutes} minutes focused",
            style = MaterialTheme.typography.titleLarge
        )
    }
}
