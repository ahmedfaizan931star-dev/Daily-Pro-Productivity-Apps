package com.dailyapps.stillpoint.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyapps.stillpoint.viewmodel.StillUiState
import com.dailyapps.stillpoint.viewmodel.StillViewModel

@Composable
fun FocusScreen(state: StillUiState, vm: StillViewModel) {
    val total = (state.selectedMinutes * 60).coerceAtLeast(1)
    val progress = 1f - (state.remainingSec.toFloat() / total)
    val mm = state.remainingSec / 60
    val ss = state.remainingSec % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Still session", style = MaterialTheme.typography.headlineLarge)
        Text("A protected countdown. When it ends, the minutes are logged automatically.")
        Spacer(Modifier.height(32.dp))
        Text("%02d:%02d".format(mm, ss), fontSize = 56.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(15, 25, 45, 60).forEach { m ->
                FilterChip(
                    selected = state.selectedMinutes == m,
                    onClick = { vm.setDuration(m) },
                    label = { Text("${m}m") },
                    enabled = !state.timerRunning
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { vm.toggleTimer() }) {
                Text(if (state.timerRunning) "Pause" else "Begin")
            }
            OutlinedButton(onClick = { vm.resetTimer() }) { Text("Reset") }
        }
        Spacer(Modifier.height(32.dp))
        Text("Recent still sessions", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (state.sessions.isEmpty()) {
            Text("No sessions logged yet.")
        } else {
            state.sessions.take(6).forEach { s ->
                Text("${s.minutes} min · ${s.note}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
