package com.dailyapps.sablequorum.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.sablequorum.data.Decision
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuorumRoot(vm: QuorumViewModel) {
    val nav = rememberNavController()
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = { BottomBar(nav) }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "board",
            modifier = Modifier.padding(padding)
        ) {
            composable("board") { BoardScreen(state, vm, nav) }
            composable("compose") { ComposeScreen(state, vm) }
            composable("review") { ReviewScreen(state, vm) }
            composable("insights") { InsightsScreen(state) }
        }
    }
}

@Composable
private fun BottomBar(nav: NavHostController) {
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        NavigationBarItem(
            selected = route == "board",
            onClick = { nav.navigate("board") { launchSingleTop = true } },
            icon = { Icon(Icons.Outlined.Gavel, contentDescription = null) },
            label = { Text("Board") }
        )
        NavigationBarItem(
            selected = route == "compose",
            onClick = { nav.navigate("compose") { launchSingleTop = true } },
            icon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
            label = { Text("Capture") }
        )
        NavigationBarItem(
            selected = route == "insights",
            onClick = { nav.navigate("insights") { launchSingleTop = true } },
            icon = { Icon(Icons.Outlined.Analytics, contentDescription = null) },
            label = { Text("Insights") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(state: QuorumUiState, vm: QuorumViewModel, nav: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("SableQuorum") })
        Row(
            Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("OPEN", "REVIEWED", "ALL").forEach { f ->
                FilterChip(
                    selected = state.filter == f,
                    onClick = { vm.setFilter(f) },
                    label = { Text(f.lowercase().replaceFirstChar { it.titlecase() }) }
                )
            }
        }
        if (state.visible.isEmpty()) {
            Column(
                Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No decisions yet", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text("Capture a choice so you can review it later with a cooler head.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { nav.navigate("compose") }) { Text("Open capture") }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.visible, key = { it.id }) { item ->
                    DecisionCard(item, onOpen = {
                        vm.select(item)
                        nav.navigate("review")
                    }, onDelete = { vm.delete(item) })
                }
            }
        }
    }
}

@Composable
private fun DecisionCard(item: Decision, onOpen: () -> Unit, onDelete: () -> Unit) {
    Card(
        onClick = onOpen,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                }
            }
            Text(
                "${item.domain} · ${item.status} · confidence ${item.confidence}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (item.chosen.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text("Chose: ${item.chosen}", fontWeight = FontWeight.Medium)
            }
            Text(
                formatTime(item.createdAt),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ComposeScreen(state: QuorumUiState, vm: QuorumViewModel) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopAppBar(title = { Text("Capture a decision") })
        OutlinedTextField(state.draftTitle, vm::setTitle, label = { Text("The decision") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.draftContext, vm::setContext, label = { Text("Context") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(state.draftOptions, vm::setOptions, label = { Text("Options considered") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(state.draftCriteria, vm::setCriteria, label = { Text("Criteria") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.draftChosen, vm::setChosen, label = { Text("Chosen path") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.draftExpected, vm::setExpected, label = { Text("Expected outcome") }, modifier = Modifier.fillMaxWidth())
        Text("Domain", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Work", "Money", "Health", "People", "Craft").forEach { d ->
                AssistChip(onClick = { vm.setDomain(d) }, label = { Text(if (state.draftDomain == d) "· $d") else d) })
            }
        }
        Text("Urgency: ${state.draftUrgency}")
        Slider(
            value = state.draftUrgency.toFloat(),
            onValueChange = { vm.setUrgency(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3
        )
        Text("Confidence: ${state.draftConfidence}%")
        Slider(
            value = state.draftConfidence.toFloat(),
            onValueChange = { vm.setConfidence(it.toInt()) },
            valueRange = 0f..100f
        )
        Button(onClick = vm::saveDraft, enabled = state.draftTitle.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
            Text("Lock into the quorum")
        }
        Spacer(Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(state: QuorumUiState, vm: QuorumViewModel) {
    val item = state.selected
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        TopAppBar(title = { Text("Review") })
        if (item == null) {
            Text("Select a decision from the board to close the loop.")
            return
        }
        Text(item.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Meta("Domain", item.domain)
        Meta("Chosen", item.chosen.ifBlank { "—" })
        Meta("Options", item.options.ifBlank { "—" })
        Meta("Criteria", item.criteria.ifBlank { "—" })
        Meta("Expected", item.expectedOutcome.ifBlank { "—" })
        Meta("Confidence at capture", "${item.confidence}%")
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.reviewNotes,
            onValueChange = vm::setReviewNotes,
            label = { Text("What actually happened?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = vm::closeReview, modifier = Modifier.fillMaxWidth()) {
            Text("Mark reviewed")
        }
    }
}

@Composable
private fun Meta(label: String, value: String) {
    Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
    Text(value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(state: QuorumUiState) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text("Insights") })
        StatCard("Open decisions", state.openCount.toString())
        Spacer(Modifier.height(12.dp))
        StatCard("Reviewed", state.reviewedCount.toString())
        Spacer(Modifier.height(12.dp))
        StatCard("Average confidence", "${state.avgConfidence}%")
        Spacer(Modifier.height(12.dp))
        StatCard("Total logged", state.decisions.size.toString())
        Spacer(Modifier.height(20.dp))
        Text(
            "SableQuorum is a private ledger of choices. The value is not speed — it is the review.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.displaySmall)
        }
    }
}

private fun formatTime(millis: Long): String {
    return SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.getDefault()).format(Date(millis))
}
