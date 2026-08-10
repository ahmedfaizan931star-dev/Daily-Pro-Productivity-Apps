package com.dailyapps.momentumvault.ui.screens

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
import com.dailyapps.momentumvault.ui.components.MomentumScoreCard
import com.dailyapps.momentumvault.ui.components.SectionHeader
import com.dailyapps.momentumvault.ui.components.StatChip
import com.dailyapps.momentumvault.viewmodel.MomentumViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HomeScreen(viewModel: MomentumViewModel) {
    val state by viewModel.uiState.collectAsState()
    val todayFormatted = LocalDate.now()
        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "MomentumVault",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = todayFormatted,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        MomentumScoreCard(score = state.momentumScore)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatChip(
                label = "Focus min",
                value = "${state.focusMinutesToday}",
                modifier = Modifier.weight(1f)
            )
            StatChip(
                label = "Habits",
                value = "${state.completedHabitsToday}/${state.habits.size}",
                modifier = Modifier.weight(1f)
            )
            StatChip(
                label = "Reflect",
                value = if (state.todayEnergy != null) "Done" else "—",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("How it works")
        Text(
            text = "Your Momentum score blends completed habits (40%), focused minutes (40%) and daily reflection (20%). Keep the streak alive and watch the score climb.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader("Quick tips")
        Text(
            text = "• Start a 25-min focus block when energy is high\n• Mark habits as soon as you finish them\n• Close the day with a short energy + mood note",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
