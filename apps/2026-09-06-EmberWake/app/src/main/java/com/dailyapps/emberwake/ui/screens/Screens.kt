package com.dailyapps.emberwake.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.emberwake.domain.Commitment
import com.dailyapps.emberwake.domain.EmberState
import com.dailyapps.emberwake.domain.RitualStep

@Composable
fun TodayScreen(
    state: EmberState,
    onEnergy: (Int) -> Unit,
    onDraft: (String) -> Unit,
    onAdd: () -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit,
    onSnapshot: () -> Unit
) {
    val done = state.commitments.count { it.done }
    val total = state.commitments.size
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("EmberWake", style = MaterialTheme.typography.headlineLarge)
            Text("Protect the first fire of the day.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Energy today", fontWeight = FontWeight.SemiBold)
                    Text("${state.energy} / 10", style = MaterialTheme.typography.titleLarge)
                    Slider(
                        value = state.energy.toFloat(),
                        onValueChange = { onEnergy(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8
                    )
                    val hint = when {
                        state.energy <= 3 -> "Protect recovery. One commitment only."
                        state.energy <= 6 -> "Steady day. Three shallow blocks."
                        else -> "High flame. Use it on the hardest work first."
                    }
                    Text(hint, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            Text("Commitments", style = MaterialTheme.typography.titleLarge)
            if (total > 0) {
                LinearProgressIndicator(
                    progress = done.toFloat() / total,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Text("$done of $total closed", style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            OutlinedTextField(
                value = state.draft,
                onValueChange = onDraft,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Add one commitment") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = onAdd, enabled = state.draft.isNotBlank()) { Text("Add") }
        }
        items(state.commitments, key = { it.id }) { item ->
            CommitmentRow(item, onToggle, onRemove)
        }
        item {
            FilledTonalButton(onClick = onSnapshot, modifier = Modifier.fillMaxWidth()) {
                Text("Save today's snapshot")
            }
        }
    }
}

@Composable
private fun CommitmentRow(
    item: Commitment,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.done, onCheckedChange = { onToggle(item.id) })
            Column(Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Medium)
                Text("Energy cost ${item.energyCost}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onRemove(item.id) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
fun RitualScreen(
    title: String,
    subtitle: String,
    steps: List<RitualStep>,
    onToggle: (String) -> Unit
) {
    val done = steps.count { it.done }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LinearProgressIndicator(progress = done.toFloat() / steps.size.coerceAtLeast(1), modifier = Modifier.fillMaxWidth())
        Text("$done / ${steps.size}")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(steps, key = { it.id }) { step ->
                Card(shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = step.done, onCheckedChange = { onToggle(step.id) })
                        Text(step.title, modifier = Modifier.weight(1f))
                        if (step.done) Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun InsightsScreen(state: EmberState) {
    val avg = if (state.history.isEmpty()) state.energy.toDouble()
    else state.history.map { it.energy }.average()
    val wakeRate = if (state.history.isEmpty()) 0.0
    else state.history.map { it.wakeDone / 5.0 }.average()
    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Insights", style = MaterialTheme.typography.headlineLarge)
            Text("A quiet ledger of how you start and close days.")
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                        Text("21-day pulse", fontWeight = FontWeight.SemiBold)
                    }
                    Text("Average energy  ${\"%.1f\".format(avg)} / 10", style = MaterialTheme.typography.titleLarge)
                    Text("Wake ritual completion  ${(wakeRate * 100).toInt()}%")
                    Text("Snapshots stored  ${state.history.size}")
                }
            }
        }
        items(state.history.asReversed()) { log ->
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(log.date, fontWeight = FontWeight.SemiBold)
                    Text("Energy ${log.energy} · Wake ${log.wakeDone}/5 · Shutdown ${log.shutDone}/5")
                    Text("Commitments ${log.commitmentsDone}/${log.commitmentsTotal}")
                }
            }
        }
        if (state.history.isEmpty()) {
            item { Text("Save a snapshot on Today to start the ledger.") }
        }
    }
}

object Destinations {
    const val Today = "today"
    const val Wake = "wake"
    const val Shut = "shut"
    const val Insights = "insights"
}

val NavIcons = listOf(
    Triple(Destinations.Today, "Today", Icons.Outlined.WbSunny),
    Triple(Destinations.Wake, "Wake", Icons.Outlined.AutoAwesome),
    Triple(Destinations.Shut, "Close", Icons.Outlined.Nightlight),
    Triple(Destinations.Insights, "Pulse", Icons.Outlined.CheckCircle)
)
