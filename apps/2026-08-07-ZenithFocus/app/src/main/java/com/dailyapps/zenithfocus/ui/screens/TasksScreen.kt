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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.zenithfocus.data.model.EnergyLevel
import com.dailyapps.zenithfocus.data.model.MatrixQuadrant
import com.dailyapps.zenithfocus.ui.components.SectionHeader
import com.dailyapps.zenithfocus.ui.components.TaskItem
import com.dailyapps.zenithfocus.viewmodel.ZenithViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: ZenithViewModel) {
    val tasks by viewModel.allTasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf<MatrixQuadrant?>(null) }

    val filtered = if (filter == null) tasks else tasks.filter { it.quadrant == filter }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Task Matrix",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Organize by urgency & importance (Eisenhower)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = filter == null,
                        onClick = { filter = null },
                        label = { Text("All") }
                    )
                    MatrixQuadrant.entries.forEach { q ->
                        FilterChip(
                            selected = filter == q,
                            onClick = { filter = if (filter == q) null else q },
                            label = {
                                Text(
                                    when (q) {
                                        MatrixQuadrant.DO_FIRST -> "Do First"
                                        MatrixQuadrant.SCHEDULE -> "Schedule"
                                        MatrixQuadrant.DELEGATE -> "Delegate"
                                        MatrixQuadrant.ELIMINATE -> "Eliminate"
                                    }
                                )
                            }
                        )
                    }
                }
            }

            item {
                SectionHeader(title = "Your Tasks (${filtered.size})")
            }

            if (filtered.isEmpty()) {
                item {
                    Text(
                        text = "No tasks yet. Tap + to add your first priority.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(filtered, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(task) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, notes, quadrant, energy ->
                viewModel.addTask(title, notes, quadrant, energy)
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, MatrixQuadrant, EnergyLevel) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var quadrant by remember { mutableStateOf(MatrixQuadrant.DO_FIRST) }
    var energy by remember { mutableStateOf(EnergyLevel.MEDIUM) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Priority") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Quadrant", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    MatrixQuadrant.entries.forEach { q ->
                        FilterChip(
                            selected = quadrant == q,
                            onClick = { quadrant = q },
                            label = {
                                Text(
                                    when (q) {
                                        MatrixQuadrant.DO_FIRST -> "Do"
                                        MatrixQuadrant.SCHEDULE -> "Sch"
                                        MatrixQuadrant.DELEGATE -> "Del"
                                        MatrixQuadrant.ELIMINATE -> "Elim"
                                    }
                                )
                            }
                        )
                    }
                }
                Text("Energy required", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    EnergyLevel.entries.forEach { e ->
                        FilterChip(
                            selected = energy == e,
                            onClick = { energy = e },
                            label = { Text(e.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) onConfirm(title, notes, quadrant, energy)
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
