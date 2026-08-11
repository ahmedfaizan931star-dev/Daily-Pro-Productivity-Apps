package com.dailyapps.cadencecore.ui.screens

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.cadencecore.ui.components.SectionHeader
import com.dailyapps.cadencecore.ui.components.StatCard
import com.dailyapps.cadencecore.viewmodel.CadenceViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(viewModel: CadenceViewModel) {
    val state by viewModel.uiState.collectAsState()
    val today = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
    )

    val completedHabits = state.habits.count { it.todayCount >= it.habit.targetPerDay }
    val totalHabits = state.habits.size.coerceAtLeast(1)
    val progress = completedHabits.toFloat() / totalHabits

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        SectionHeader(
            title = "CadenceCore",
            subtitle = today
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Today's rhythm",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$completedHabits of ${state.habits.size} habits completed",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Habits",
                value = "$completedHabits/${state.habits.size}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Focus min",
                value = "${state.focusMinutesToday}",
                accent = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Sessions",
                value = "${state.sessionsToday.size}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Mood",
                value = when (state.reflection?.mood ?: state.mood) {
                    1 -> "😔"
                    2 -> "😐"
                    3 -> "🙂"
                    4 -> "😊"
                    else -> "🤩"
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Quick habits",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (state.habits.isEmpty()) {
            Text(
                text = "Add habits in the Habits tab to start building your cadence.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            state.habits.take(4).forEach { item ->
                val done = item.todayCount >= item.habit.targetPerDay
                Text(
                    text = (if (done) "✓ " else "○ ") + item.habit.title +
                            if (item.streak > 0) "  · ${item.streak} day streak" else "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (done) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tip: Consistency beats intensity. Keep your daily cadence light and repeatable.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
    }
}
