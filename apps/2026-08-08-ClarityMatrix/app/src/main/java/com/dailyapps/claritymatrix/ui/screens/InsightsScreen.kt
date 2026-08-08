package com.dailyapps.claritymatrix.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.claritymatrix.data.model.Quadrant
import com.dailyapps.claritymatrix.ui.components.StatChip
import com.dailyapps.claritymatrix.ui.components.TaskCard
import com.dailyapps.claritymatrix.ui.components.quadrantColor
import com.dailyapps.claritymatrix.ui.theme.MatrixCyan
import com.dailyapps.claritymatrix.ui.theme.MatrixEmerald
import com.dailyapps.claritymatrix.ui.theme.MatrixIndigo
import com.dailyapps.claritymatrix.viewmodel.ClarityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(viewModel: ClarityViewModel) {
    val allTasks by viewModel.allTasks.collectAsState()
    val completedToday by viewModel.completedToday.collectAsState()
    val active = allTasks.filter { !it.isCompleted }
    val completed = allTasks.filter { it.isCompleted }.sortedByDescending { it.completedAt }

    val byQuadrant = Quadrant.entries.associateWith { q ->
        active.count { it.quadrant == q }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Insights", fontWeight = FontWeight.Bold)
                        Text(
                            "Your productivity at a glance",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatChip(label = "Active", value = active.size.toString(), color = MatrixIndigo)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatChip(label = "Done today", value = completedToday.toString(), color = MatrixEmerald)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatChip(label = "Total done", value = completed.size.toString(), color = MatrixCyan)
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Active by quadrant",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(12.dp))
                        byQuadrant.forEach { (q, count) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = when (q) {
                                        Quadrant.DO_FIRST -> "Do First"
                                        Quadrant.SCHEDULE -> "Schedule"
                                        Quadrant.DELEGATE -> "Delegate"
                                        Quadrant.ELIMINATE -> "Eliminate"
                                    },
                                    color = quadrantColor(q)
                                )
                                Text(
                                    text = count.toString(),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            if (completed.isNotEmpty()) {
                item {
                    Text(
                        "Recently completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(completed.take(10), key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { viewModel.toggleComplete(task) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
