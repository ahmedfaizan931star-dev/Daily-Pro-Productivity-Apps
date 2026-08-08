package com.dailyapps.claritymatrix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.claritymatrix.data.model.Quadrant
import com.dailyapps.claritymatrix.ui.components.QuadrantHeader
import com.dailyapps.claritymatrix.ui.components.TaskCard
import com.dailyapps.claritymatrix.ui.components.quadrantColor
import com.dailyapps.claritymatrix.ui.components.quadrantLabel
import com.dailyapps.claritymatrix.viewmodel.ClarityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatrixScreen(viewModel: ClarityViewModel) {
    val tasks by viewModel.activeTasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val grouped = remember(tasks) {
        Quadrant.entries.associateWith { q -> tasks.filter { it.quadrant == q } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ClarityMatrix", fontWeight = FontWeight.Bold)
                        Text(
                            "Eisenhower prioritization",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Quadrant.entries.forEach { quadrant ->
                val list = grouped[quadrant].orEmpty()
                item {
                    QuadrantHeader(
                        title = quadrantLabel(quadrant),
                        color = quadrantColor(quadrant),
                        count = list.size
                    )
                }
                items(list, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { viewModel.toggleComplete(task) },
                        onDelete = { viewModel.deleteTask(task.id) },
                        onStartFocus = { viewModel.startTimer(task.estimatedMinutes, task) }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, notes, quadrant, minutes ->
                viewModel.addTask(title, notes, quadrant, minutes)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Quadrant, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(Quadrant.DO_FIRST) }
    var minutes by remember { mutableStateOf("25") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Task") },
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
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    Quadrant.entries.forEachIndexed { index, q ->
                        SegmentedButton(
                            selected = selected == q,
                            onClick = { selected = q },
                            shape = SegmentedButtonDefaults.itemShape(index, Quadrant.entries.size)
                        ) {
                            Text(quadrantLabel(q).split(" ").first(), maxLines = 1)
                        }
                    }
                }
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter { c -> c.isDigit() }.take(3) },
                    label = { Text("Est. minutes") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, notes, selected, minutes.toIntOrNull() ?: 25)
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
