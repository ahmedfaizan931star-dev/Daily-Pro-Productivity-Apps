package com.dailyapps.prismflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.prismflow.data.model.PriorityQuadrant
import com.dailyapps.prismflow.data.model.Task
import com.dailyapps.prismflow.viewmodel.PrismViewModel

@Composable
fun MatrixScreen(viewModel: PrismViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var showAdd by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var selectedQuadrant by remember { mutableStateOf(PriorityQuadrant.URGENT_IMPORTANT) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Eisenhower Matrix", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        val quadrants = listOf(
            PriorityQuadrant.URGENT_IMPORTANT to "Do First",
            PriorityQuadrant.NOT_URGENT_IMPORTANT to "Schedule",
            PriorityQuadrant.URGENT_NOT_IMPORTANT to "Delegate",
            PriorityQuadrant.NOT_URGENT_NOT_IMPORTANT to "Eliminate"
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
            quadrants.forEach { (quadrant, label) ->
                item {
                    val qTasks = tasks.filter { it.quadrant == quadrant && !it.isCompleted }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when (quadrant) {
                                PriorityQuadrant.URGENT_IMPORTANT -> MaterialTheme.colorScheme.errorContainer
                                PriorityQuadrant.NOT_URGENT_IMPORTANT -> MaterialTheme.colorScheme.primaryContainer
                                PriorityQuadrant.URGENT_NOT_IMPORTANT -> MaterialTheme.colorScheme.secondaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("$label (${qTasks.size})", style = MaterialTheme.typography.titleMedium)
                            qTasks.forEach { task ->
                                TaskRow(task, viewModel)
                            }
                        }
                    }
                }
            }
        }

        if (showAdd) {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("New task") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PriorityQuadrant.entries.forEach { q ->
                    androidx.compose.material3.FilterChip(
                        selected = selectedQuadrant == q,
                        onClick = { selectedQuadrant = q },
                        label = { Text(q.name.take(2)) }
                    )
                }
            }
            Row {
                FloatingActionButton(onClick = {
                    if (newTitle.isNotBlank()) {
                        viewModel.addTask(newTitle, quadrant = selectedQuadrant)
                        newTitle = ""
                        showAdd = false
                    }
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
            }
        } else {
            FloatingActionButton(
                onClick = { showAdd = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
private fun TaskRow(task: Task, viewModel: PrismViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(task.title, modifier = Modifier.weight(1f))
        IconButton(onClick = { viewModel.toggleTask(task) }) {
            Icon(Icons.Default.Check, contentDescription = "Complete")
        }
        IconButton(onClick = { viewModel.deleteTask(task.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
