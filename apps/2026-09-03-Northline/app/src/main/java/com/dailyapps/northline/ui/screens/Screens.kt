package com.dailyapps.northline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dailyapps.northline.NorthlineViewModel
import com.dailyapps.northline.domain.Commitment
import com.dailyapps.northline.domain.EnergyBand
import com.dailyapps.northline.domain.Friction
import com.dailyapps.northline.domain.NorthlineState

@Composable
fun NorthlineApp(state: NorthlineState, vm: NorthlineViewModel, nav: NavHostController) {
    val route = nav.currentBackStackEntryAsState().value?.destination?.route ?: "today"
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "today",
                    onClick = { nav.navigate("today") },
                    icon = { Icon(Icons.Outlined.Flag, contentDescription = null) },
                    label = { Text("Today") }
                )
                NavigationBarItem(
                    selected = route == "queue",
                    onClick = { nav.navigate("queue") },
                    icon = { Icon(Icons.Outlined.FormatListBulleted, contentDescription = null) },
                    label = { Text("Queue") }
                )
                NavigationBarItem(
                    selected = route == "review",
                    onClick = { nav.navigate("review") },
                    icon = { Icon(Icons.Outlined.DoneAll, contentDescription = null) },
                    label = { Text("Review") }
                )
                NavigationBarItem(
                    selected = route == "insights",
                    onClick = { nav.navigate("insights") },
                    icon = { Icon(Icons.Outlined.Insights, contentDescription = null) },
                    label = { Text("Insights") }
                )
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = "today", modifier = Modifier.padding(padding)) {
            composable("today") { TodayScreen(state, vm) }
            composable("queue") { QueueScreen(state, vm) }
            composable("review") { ReviewScreen(state, vm) }
            composable("insights") { InsightsScreen(state) }
        }
    }
}

@Composable
fun TodayScreen(state: NorthlineState, vm: NorthlineViewModel) {
    val plan = state.today
    val done = plan.commitments.count { it.done }
    val total = plan.commitments.size.coerceAtLeast(1)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Northline", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
            Text("Hold one line.", style = MaterialTheme.typography.displaySmall)
            Text(plan.dateKey, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Today's north star", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = plan.northStar,
                        onValueChange = vm::setNorthStar,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("What must be true by evening?") }
                    )
                }
            }
        }
        item {
            Text("Alignment ${done}/$total", fontWeight = FontWeight.Medium)
            LinearProgressIndicator(
                progress = done / total.toFloat(),
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
            )
        }
        items(plan.commitments, key = { it.id }) { item ->
            CommitmentCard(item, onToggle = { vm.toggle(item.id) }, onDelete = { vm.remove(item.id) })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QueueScreen(state: NorthlineState, vm: NorthlineViewModel) {
    var title by remember { mutableStateOf("") }
    var leverage by remember { mutableFloatStateOf(3f) }
    var energy by remember { mutableStateOf(EnergyBand.STEADY) }
    var friction by remember { mutableStateOf(Friction.NONE) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Capture the line", style = MaterialTheme.typography.headlineMedium)
            Text("Rank by leverage so shallow work cannot crowd the north star.")
        }
        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Commitment") }
            )
            Text("Leverage ${leverage.toInt()}", modifier = Modifier.padding(top = 8.dp))
            Slider(value = leverage, onValueChange = { leverage = it }, valueRange = 1f..5f, steps = 3)
            Text("Energy")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EnergyBand.entries.forEach { band ->
                    FilterChip(selected = energy == band, onClick = { energy = band }, label = { Text(band.name) })
                }
            }
            Text("Friction", modifier = Modifier.padding(top = 8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Friction.entries.forEach { tag ->
                    FilterChip(selected = friction == tag, onClick = { friction = tag }, label = { Text(tag.name) })
                }
            }
            Button(
                onClick = {
                    vm.addCommitment(title, leverage.toInt(), energy, friction)
                    title = ""
                },
                modifier = Modifier.padding(top = 12.dp)
            ) { Text("Add to queue") }
        }
        items(state.today.commitments, key = { it.id }) { item ->
            CommitmentCard(item, onToggle = { vm.toggle(item.id) }, onDelete = { vm.remove(item.id) })
        }
    }
}

@Composable
fun ReviewScreen(state: NorthlineState, vm: NorthlineViewModel) {
    val plan = state.today
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Close the line", style = MaterialTheme.typography.headlineMedium)
        StatRow("Streak", "${state.streak} days")
        StatRow("Weekly wins", "${state.weeklyWins}")
        StatRow("Alignment", if (plan.closed) "${plan.alignmentScore}" else "Open")
        if (!plan.closed) {
            Button(onClick = vm::closeDay, modifier = Modifier.fillMaxWidth()) {
                Text("Score and close today")
            }
        } else {
            Text("Today closed at ${plan.alignmentScore}. The north star was: ${plan.northStar}")
            TextButton(onClick = vm::reopen) { Text("Reopen the day") }
        }
        Text("Recent closes", style = MaterialTheme.typography.titleLarge)
        state.history.take(7).forEach { day ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(day.dateKey, fontWeight = FontWeight.Medium)
                        Text(day.northStar.ifBlank { "No star set" }, style = MaterialTheme.typography.bodyLarge)
                    }
                    Text("${day.alignmentScore}")
                }
            }
        }
    }
}

@Composable
fun InsightsScreen(state: NorthlineState) {
    val all = state.history.flatMap { it.commitments } + state.today.commitments
    val energyCounts = EnergyBand.entries.associateWith { band -> all.count { it.energy == band } }
    val frictionOpen = Friction.entries.associateWith { tag -> all.count { it.friction == tag && !it.done } }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Where the line bends", style = MaterialTheme.typography.headlineMedium)
        Text("Energy mix")
        energyCounts.forEach { (band, count) ->
            InsightBar(band.name, count, all.size.coerceAtLeast(1))
        }
        Text("Open friction")
        frictionOpen.forEach { (tag, count) ->
            InsightBar(tag.name, count, all.size.coerceAtLeast(1))
        }
        Card {
            Column(Modifier.padding(16.dp)) {
                Icon(Icons.Outlined.Bolt, contentDescription = null)
                Spacer(Modifier.height(8.dp))
                Text("Protect deep-energy work before noon. Queue low-energy recovery after the north star is visibly moving.")
            }
        }
    }
}

@Composable
private fun InsightBar(label: String, value: Int, max: Int) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label)
            Text("$value")
        }
        LinearProgressIndicator(
            progress = value / max.toFloat(),
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CommitmentCard(item: Commitment, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = item.done, onCheckedChange = { onToggle() })
            Column(Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Medium)
                Text(
                    "Leverage ${item.leverage} · ${item.energy.name} · ${item.friction.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, contentDescription = "Remove")
            }
        }
    }
}
