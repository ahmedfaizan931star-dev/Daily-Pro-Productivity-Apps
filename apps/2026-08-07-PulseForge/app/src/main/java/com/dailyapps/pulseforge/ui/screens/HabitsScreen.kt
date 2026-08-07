package com.dailyapps.pulseforge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.pulseforge.data.model.Habit
import com.dailyapps.pulseforge.ui.components.AddItemDialog
import com.dailyapps.pulseforge.ui.components.EmptyState
import com.dailyapps.pulseforge.ui.theme.PulseEmerald
import com.dailyapps.pulseforge.viewmodel.PulseViewModel

@Composable
fun HabitsScreen(viewModel: PulseViewModel) {
    val habits by viewModel.habits.collectAsState()
    val completions by viewModel.habitCompletions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    BoxWithFab(
        onFabClick = { showDialog = true }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Habit Forge",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Build consistency. Mark habits complete each day to grow your streak.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (habits.isEmpty()) {
                EmptyState(
                    message = "No habits yet. Forge your first one!",
                    actionLabel = "Add Habit",
                    onAction = { showDialog = true }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(habits, key = { it.id }) { habit ->
                        HabitCard(
                            habit = habit,
                            isCompleted = completions.contains(habit.id),
                            onToggle = { viewModel.toggleHabit(habit.id) },
                            onDelete = { viewModel.deleteHabit(habit) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showDialog) {
        AddItemDialog(
            title = "New Habit",
            placeholder = "e.g. Morning stretch, Read 20 pages…",
            onDismiss = { showDialog = false },
            onConfirm = { viewModel.addHabit(it) }
        )
    }
}

@Composable
private fun HabitCard(
    habit: Habit,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Toggle",
                    tint = if (isCompleted) PulseEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isCompleted) "Completed today" else "Tap to complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun BoxWithFab(
    onFabClick: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
        content()
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Add habit")
        }
    }
}
