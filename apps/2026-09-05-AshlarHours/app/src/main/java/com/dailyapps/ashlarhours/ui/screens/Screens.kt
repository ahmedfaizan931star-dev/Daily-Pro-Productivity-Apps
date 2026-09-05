package com.dailyapps.ashlarhours.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.ashlarhours.AshlarUiState
import com.dailyapps.ashlarhours.domain.HourStone
import com.dailyapps.ashlarhours.domain.StoneKind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshlarApp(
    state: AshlarUiState,
    onSelectHour: (Int?) -> Unit,
    onTitle: (String) -> Unit,
    onKind: (StoneKind) -> Unit,
    onPlace: () -> Unit,
    onToggle: (String) -> Unit,
    onClear: (Int) -> Unit,
    onTemplate: (String) -> Unit,
    onTarget: (Int) -> Unit
) {
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column {
                    Text("Ashlar Hours")
                    Text(
                        state.todayLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Outlined.Today, null) }, label = { Text("Mason") })
                NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Outlined.Insights, null) }, label = { Text("Balance") })
                NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Outlined.Tune, null) }, label = { Text("Rituals") })
            }
        }
    ) { padding ->
        when (tab) {
            0 -> MasonScreen(padding, state, onSelectHour, onTitle, onKind, onPlace, onToggle, onClear)
            1 -> BalanceScreen(padding, state)
            else -> RitualsScreen(padding, state, onTemplate, onTarget)
        }
    }
}

@Composable
private fun MasonScreen(
    padding: PaddingValues,
    state: AshlarUiState,
    onSelectHour: (Int?) -> Unit,
    onTitle: (String) -> Unit,
    onKind: (StoneKind) -> Unit,
    onPlace: () -> Unit,
    onToggle: (String) -> Unit,
    onClear: (Int) -> Unit
) {
    val hours = (7..20).toList()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Lay load-bearing hours like cut stone. Empty slots are joints — keep a few open.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(hours) { hour ->
            val stone = state.stones.find { it.hour == hour }
            HourRow(hour, stone, state.selectedHour == hour, onSelectHour, onToggle)
        }
        if (state.selectedHour != null) {
            item {
                EditorCard(state, onTitle, onKind, onPlace, onClear, onSelectHour)
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun HourRow(
    hour: Int,
    stone: HourStone?,
    selected: Boolean,
    onSelectHour: (Int?) -> Unit,
    onToggle: (String) -> Unit
) {
    val label = "%02d:00".format(hour)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectHour(if (selected) null else hour) }
            .then(
                if (selected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                else Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(64.dp))
            Box(
                Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(kindColor(stone?.kind ?: StoneKind.EMPTY))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    stone?.title ?: "Open joint",
                    fontWeight = if (stone == null) FontWeight.Normal else FontWeight.Medium
                )
                Text(
                    stone?.kind?.label ?: "Tap to place a stone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (stone != null) {
                Icon(
                    imageVector = if (stone.done) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "toggle",
                    modifier = Modifier.clickable { onToggle(stone.id) },
                    tint = if (stone.done) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditorCard(
    state: AshlarUiState,
    onTitle: (String) -> Unit,
    onKind: (StoneKind) -> Unit,
    onPlace: () -> Unit,
    onClear: (Int) -> Unit,
    onSelectHour: (Int?) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Place stone at %02d:00".format(state.selectedHour ?: 0), style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = state.draftTitle,
                onValueChange = onTitle,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("What is this hour for?") },
                singleLine = true
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StoneKind.entries.filter { it != StoneKind.EMPTY }.forEach { kind ->
                    FilterChip(
                        selected = state.draftKind == kind,
                        onClick = { onKind(kind) },
                        label = { Text(kind.shortLabel) }
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onPlace) { Text("Set stone") }
                OutlinedButton(onClick = { state.selectedHour?.let(onClear) }) { Text("Clear") }
                TextButton(onClick = { onSelectHour(null) }) { Text("Close") }
            }
        }
    }
}

@Composable
private fun BalanceScreen(padding: PaddingValues, state: AshlarUiState) {
    val i = state.insights
    val progress = if (state.deepTarget == 0) 0f else (i.deepHours.toFloat() / state.deepTarget).coerceIn(0f, 1f)
    Column(
        Modifier.fillMaxSize().padding(padding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Day balance", style = MaterialTheme.typography.headlineMedium)
        Text(i.balanceNote, style = MaterialTheme.typography.bodyLarge)
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(20.dp)))
        Text("${i.deepHours} / ${state.deepTarget} deep hours")
        StatRow("Deep", i.deepHours, kindColor(StoneKind.DEEP))
        StatRow("Admin", i.adminHours, kindColor(StoneKind.ADMIN))
        StatRow("People", i.socialHours, kindColor(StoneKind.SOCIAL))
        StatRow("Recovery", i.recoveryHours, kindColor(StoneKind.RECOVERY))
        Card(shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Completion", style = MaterialTheme.typography.titleMedium)
                Text("${i.completed} of ${i.planned} stones marked done")
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(color))
        Spacer(Modifier.width(10.dp))
        Text(label, modifier = Modifier.weight(1f))
        Text("$value h", fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RitualsScreen(
    padding: PaddingValues,
    state: AshlarUiState,
    onTemplate: (String) -> Unit,
    onTarget: (Int) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(padding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Ritual templates", style = MaterialTheme.typography.headlineMedium)
        Text("Drop a proven day-shape onto the mason board. Existing stones are replaced.")
        Button(onClick = { onTemplate("maker") }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.AutoAwesome, null)
            Spacer(Modifier.width(8.dp))
            Text("Maker day — two deep quarries")
        }
        OutlinedButton(onClick = { onTemplate("manager") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manager day — people + decisions")
        }
        OutlinedButton(onClick = { onTemplate("light") }, modifier = Modifier.fillMaxWidth()) {
            Text("Light day — four keystones only")
        }
        Text("Deep-work target", style = MaterialTheme.typography.titleMedium)
        Text("${state.deepTarget} hours")
        Slider(
            value = state.deepTarget.toFloat(),
            onValueChange = { onTarget(it.toInt()) },
            valueRange = 1f..8f,
            steps = 6
        )
    }
}

@Composable
private fun kindColor(kind: StoneKind): Color = when (kind) {
    StoneKind.DEEP -> Color(0xFF2C4A3E)
    StoneKind.ADMIN -> Color(0xFF4A5A6A)
    StoneKind.RECOVERY -> Color(0xFF8B6B4A)
    StoneKind.SOCIAL -> Color(0xFF3D5A73)
    StoneKind.EMPTY -> Color(0xFFC9BBA8)
}
