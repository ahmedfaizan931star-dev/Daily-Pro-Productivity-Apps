package com.dailyapps.luminafocus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.luminafocus.ui.components.SectionHeader
import com.dailyapps.luminafocus.ui.components.StatCard
import com.dailyapps.luminafocus.ui.theme.EnergyHigh
import com.dailyapps.luminafocus.ui.theme.LuminaPrimary
import com.dailyapps.luminafocus.ui.theme.LuminaSecondary
import com.dailyapps.luminafocus.ui.theme.MoodPositive
import com.dailyapps.luminafocus.viewmodel.LuminaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(viewModel: LuminaViewModel) {
    val state by viewModel.uiState.collectAsState()
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
    val habitsDone = state.habits.count { it.lastCompletedDate == LocalDate.now().toString() }
    val avgEnergy = if (state.energyLogs.isNotEmpty()) {
        state.energyLogs.take(5).map { it.energyLevel }.average().toFloat()
    } else 0f
    val avgMood = if (state.energyLogs.isNotEmpty()) {
        state.energyLogs.take(5).map { it.mood }.average().toFloat()
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "LuminaFocus",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = today,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("Today's Overview")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Focus",
                value = "${state.todayMinutes}m",
                subtitle = "${state.todaySessions.size} sessions",
                accent = LuminaPrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Habits",
                value = "$habitsDone/${state.habits.size}",
                subtitle = "completed",
                accent = LuminaSecondary,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Energy",
                value = if (avgEnergy > 0) String.format("%.1f", avgEnergy) else "—",
                subtitle = "avg (1-5)",
                accent = EnergyHigh,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Mood",
                value = if (avgMood > 0) String.format("%.1f", avgMood) else "—",
                subtitle = "avg (1-5)",
                accent = MoodPositive,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader("Quick Tips")
        Text(
            text = "Start a focus session, check off micro-habits, and log your energy to build a clearer picture of your productive rhythm.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )
    }
}
