package com.dailyapps.pulseforge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.dailyapps.pulseforge.data.model.Priority
import com.dailyapps.pulseforge.ui.components.AddItemDialog
import com.dailyapps.pulseforge.ui.components.EmptyState
import com.dailyapps.pulseforge.ui.theme.PulseViolet
import com.dailyapps.pulseforge.viewmodel.PulseViewModel

@Composable
fun PrioritiesScreen(viewModel: PulseViewModel) {
    val priorities by viewModel.priorities.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Daily Forge",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Define up to 5 priorities. Focus on what truly moves the needle today.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (priorities.isEmpty()) {
                EmptyState(
                    message = "No priorities forged yet. Add your most important outcomes.",
                    actionLabel = "Add Priority",
                    onAction = { showDialog = true }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(priorities, key = { _, p -> p.id }) { index, priority ->
                        PriorityCard(
                            index = index + 1,
                            priority = priority,
                            onToggle = { viewModel.togglePriority(priority) },
                            onDelete = { viewModel.deletePriority(priority) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        if (priorities.size < 5) {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add priority")
            }
        }
    }

    if (showDialog) {
        AddItemDialog(
            title = "Forge Priority",
            placeholder = "What must get done today?",
            onDismiss = { showDialog = false },
            onConfirm = { viewModel.addPriority(it) }
        )
    }
}

@Composable
private fun PriorityCard(
    index: Int,
    priority: Priority,
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
            Text(
                text = "#$index",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PulseViolet,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (priority.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Toggle",
                    tint = if (priority.isCompleted) PulseViolet else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = priority.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (priority.isCompleted) FontWeight.Normal else FontWeight.Medium,
                color = if (priority.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
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
