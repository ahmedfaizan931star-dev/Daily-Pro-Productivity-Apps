package com.dailyapps.pebblelane.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.pebblelane.viewmodel.PebbleViewModel

@Composable
fun HarborScreen(vm: PebbleViewModel) {
    val meetings by vm.meetings.collectAsState()
    val blocks by vm.blocks.collectAsState()
    val closeouts by vm.closeouts.collectAsState()
    val meetingMin = meetings.sumOf { it.minutes }
    val drainAvg = if (meetings.isEmpty()) 0 else meetings.sumOf { it.drain } / meetings.size
    val protectedMin = blocks.filter { it.done }.sumOf { it.minutes }
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PebbleLane", style = MaterialTheme.typography.headlineMedium)
        Text("Protect the lane. Log the drain. Close the day.")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Meetings", "$meetingMin min", Modifier.weight(1f))
            StatCard("Avg drain", "$drainAvg / 5", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Protected", "$protectedMin min", Modifier.weight(1f))
            StatCard("Close-outs", "${closeouts.size}", Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
