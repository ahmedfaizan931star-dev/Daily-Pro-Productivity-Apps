package com.dailyapps.stillpoint.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.stillpoint.viewmodel.StillUiState

@Composable
fun InsightsScreen(state: StillUiState) {
    val totalMin = state.sessions.sumOf { it.minutes }
    val avgEnergy = state.intentions.map { it.energy }.average().takeIf { !it.isNaN() } ?: 0.0
    val completion = if (state.intentions.isEmpty()) 0f
    else state.intentions.count { it.completed }.toFloat() / state.intentions.size
    val protectedMin = state.blocks.filter { it.enabled }.sumOf { it.durationMin }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Pulse", style = MaterialTheme.typography.headlineLarge)
        Text("A calm readout of how well you guarded attention this week.")
        Spacer(Modifier.height(20.dp))

        InsightCard("Logged focus", "$totalMin minutes")
        InsightCard("Protected calendar", "$protectedMin minutes / weekday")
        InsightCard("Intention close rate", "${(completion * 100).toInt()}%")
        InsightCard("Average energy asked", String.format("%.1f / 5", avgEnergy))

        Spacer(Modifier.height(8.dp))
        Text("Close rate", fontWeight = FontWeight.Medium)
        LinearProgressIndicator(progress = { completion }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))
        Text(
            "Stillpoint works best when quiet blocks stay few and sacred. " +
                "If close rate drops below 50%, shrink the day's intention instead of adding hours."
        )
    }
}

@Composable
private fun InsightCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
