package com.dailyapps.zenithfocus.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier.modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.zenithfocus.data.model.EnergyLevel
import com.dailyapps.zenithfocus.ui.components.SectionHeader
import com.dailyapps.zenithfocus.ui.components.StatCard
import com.dailyapps.zenithfocus.ui.components.TaskItem
import com.dailyapps.zenithfocus.ui.theme.EnergyHigh
import com.dailyapps.zenithfocus.ui.theme.EnergyLow
import com.dailyapps.zenithfocus.ui.theme.EnergyMedium
import com.dailyapps.zenithfocus.viewmodel.ZenithViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HomeScreen(
    viewModel: ZenithViewModel,
    onNavigateToTasks: () -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToReflect: () -> Unit
) {
    val state by viewModel.homeState.collectAsState()
    val today = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Zenith Focus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = today,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Focus Today",
                    value = "${state.focusMinutesToday}m",
                    subtitle = "deep work",
                    accent = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tasks Done",
                    value = "${state.completedTasksToday}",
                    subtitle = "completed",
                    accent = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            SectionHeader(title = "Quick Actions")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = onNavigateToFocus,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Focus")
                }
                FilledTonalButton(
                    onClick = onNavigateToTasks,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Tasks")
                }
            }
        }

        item {
            SectionHeader(title = "Current Energy")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "morning" to "Morning",
                    "afternoon" to "Afternoon",
                    "evening" to "Evening"
                ).forEach { (period, label) ->
                    val selected = when (period) {
                        "morning" -> state.todayReflection?.energyMorning
                        "afternoon" -> state.todayReflection?.energyAfternoon
                        else -> state.todayReflection?.energyEvening
                    }
                    EnergyButton(
                        label = label,
                        selected = selected,
                        onSelect = { level -> viewModel.setEnergy(period, level) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Open Priorities",
                action = {
                    Button(onClick = onNavigateToTasks) {
                        Text("See all")
                    }
                }
            )
        }

        if (state.openTasks.isEmpty()) {
            item {
                Text(
                    text = "No open tasks. Add your top priorities for today.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(state.openTasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onNavigateToReflect,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null)
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Evening Reflection")
            }
        }
    }
}

@Composable
private fun EnergyButton(
    label: String,
    selected: EnergyLevel?,
    onSelect: (EnergyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(EnergyLevel.HIGH, EnergyLevel.MEDIUM, EnergyLevel.LOW).forEach { level ->
                val color = when (level) {
                    EnergyLevel.HIGH -> EnergyHigh
                    EnergyLevel.MEDIUM -> EnergyMedium
                    EnergyLevel.LOW -> EnergyLow
                }
                val isSelected = selected == level
                androidx.compose.material3.FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(level) },
                    label = {
                        Text(
                            text = when (level) {
                                EnergyLevel.HIGH -> "H"
                                EnergyLevel.MEDIUM -> "M"
                                EnergyLevel.LOW -> "L"
                            },
                            color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
    }
}
