package com.dailyapps.keelstone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dailyapps.keelstone.KeelViewModel
import com.dailyapps.keelstone.domain.KeelStone
import com.dailyapps.keelstone.domain.WeekState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeelApp(state: WeekState, vm: KeelViewModel, nav: NavHostController) {
    val route = nav.currentBackStackEntryAsState().value?.destination?.route ?: "keel"
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column {
                    Text("KeelStone", style = MaterialTheme.typography.titleLarge)
                    Text(state.weekLabel, style = MaterialTheme.typography.labelLarge)
                }
            })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "keel",
                    onClick = { nav.navigate("keel") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Flag, contentDescription = null) },
                    label = { Text("Keel") }
                )
                NavigationBarItem(
                    selected = route == "carve",
                    onClick = { nav.navigate("carve") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Schedule, contentDescription = null) },
                    label = { Text("Carve") }
                )
                NavigationBarItem(
                    selected = route == "review",
                    onClick = { nav.navigate("review") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
                    label = { Text("Review") }
                )
                NavigationBarItem(
                    selected = route == "capacity",
                    onClick = { nav.navigate("capacity") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Tune, contentDescription = null) },
                    label = { Text("Capacity") }
                )
            }
        },
        floatingActionButton = {
            if (route == "keel") {
                FloatingActionButton(onClick = { vm.upsertStone(vm.newStone()) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "Add stone")
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = "keel", modifier = Modifier.padding(padding)) {
            composable("keel") { KeelScreen(state, vm) }
            composable("carve") { CarveScreen(state, vm) }
            composable("review") { ReviewScreen(state, vm) }
            composable("capacity") { CapacityScreen(state, vm) }
        }
    }
}

@Composable
private fun KeelScreen(state: WeekState, vm: KeelViewModel) {
    val logged = String.format("%.1f", state.loggedHours)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Three stones keep the week from drifting.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { state.loadPercent.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                logged + " / " + state.capacityHours + "h logged",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        items(state.stones, key = { it.id }) { stone ->
            StoneCard(stone, vm)
        }
    }
}

@Composable
private fun StoneCard(stone: KeelStone, vm: KeelViewModel) {
    var title by remember(stone.id, stone.title) { mutableStateOf(stone.title) }
    var why by remember(stone.id, stone.why) { mutableStateOf(stone.why) }
    val logged = String.format("%.1f", stone.hoursLogged)
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        vm.upsertStone(stone.copy(title = it))
                    },
                    label = { Text("Keel stone") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { vm.toggleDone(stone.id) }) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = "Toggle done",
                        tint = if (stone.done) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { vm.removeStone(stone.id) }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Remove")
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = why,
                onValueChange = {
                    why = it
                    vm.upsertStone(stone.copy(why = it))
                },
                label = { Text("Why this matters") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                logged + "h of " + stone.hoursTarget + "h",
                style = MaterialTheme.typography.labelLarge
            )
            LinearProgressIndicator(
                progress = {
                    if (stone.hoursTarget == 0) 0f
                    else (stone.hoursLogged / stone.hoursTarget).coerceAtMost(1f)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun CarveScreen(state: WeekState, vm: KeelViewModel) {
    var selected by remember { mutableStateOf(state.stones.firstOrNull()?.id.orEmpty()) }
    var minutes by remember { mutableIntStateOf(50) }
    var note by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Carve a block against a stone.", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            state.stones.forEach { stone ->
                val label = stone.title.ifBlank { "Untitled" }
                FilledTonalButton(
                    onClick = { selected = stone.id },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                ) {
                    Text(if (stone.id == selected) "* " + label else label)
                }
            }
            Text("Minutes: " + minutes)
            Slider(
                value = minutes.toFloat(),
                onValueChange = { minutes = it.roundToInt() },
                valueRange = 15f..180f
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("What you will protect") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            FilledTonalButton(
                onClick = {
                    if (selected.isNotBlank()) {
                        vm.addCarve(selected, minutes, note)
                        note = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Log carve") }
            Spacer(Modifier.height(16.dp))
            Text("Recent carves", style = MaterialTheme.typography.titleLarge)
        }
        items(state.carves, key = { it.id }) { carve ->
            val stoneTitle = state.stones.firstOrNull { it.id == carve.stoneId }?.title ?: "Stone"
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(stoneTitle + " - " + carve.dayLabel + " - " + carve.minutes + "m")
                    if (carve.note.isNotBlank()) Text(carve.note, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun ReviewScreen(state: WeekState, vm: KeelViewModel) {
    var note by remember(state.reviewNote) { mutableStateOf(state.reviewNote) }
    val load = String.format("%.0f", state.loadPercent * 100)
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Weekly review", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Done: " + state.stones.count { it.done } + " / " + state.stones.size)
        Text("Load: " + load + "% of capacity")
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
                vm.setReview(it)
            },
            label = { Text("What held. What drifted.") },
            modifier = Modifier.fillMaxWidth().height(180.dp)
        )
    }
}

@Composable
private fun CapacityScreen(state: WeekState, vm: KeelViewModel) {
    var cap by remember(state.capacityHours) { mutableIntStateOf(state.capacityHours) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Weekly capacity", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Honest hours you can actually give deep work - not calendar fiction.")
        Spacer(Modifier.height(16.dp))
        Text("" + cap + " hours")
        Slider(
            value = cap.toFloat(),
            onValueChange = {
                cap = it.roundToInt()
                vm.setCapacity(cap)
            },
            valueRange = 4f..60f
        )
        state.stones.forEach { stone ->
            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(stone.title.ifBlank { "Untitled" }, modifier = Modifier.weight(1f))
                var target by remember(stone.id, stone.hoursTarget) { mutableStateOf(stone.hoursTarget.toString()) }
                OutlinedTextField(
                    value = target,
                    onValueChange = { value ->
                        target = value.filter { it.isDigit() }
                        vm.upsertStone(stone.copy(hoursTarget = target.toIntOrNull() ?: 0))
                    },
                    modifier = Modifier.width(88.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("h") }
                )
            }
        }
    }
}
