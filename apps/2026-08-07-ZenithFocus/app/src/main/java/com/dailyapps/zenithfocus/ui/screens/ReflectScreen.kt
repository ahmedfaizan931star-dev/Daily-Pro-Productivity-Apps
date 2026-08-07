package com.dailyapps.zenithfocus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.zenithfocus.viewmodel.ZenithViewModel

@Composable
fun ReflectScreen(viewModel: ZenithViewModel) {
    val reflection by viewModel.todayReflection.collectAsState()

    var wins by remember(reflection) { mutableStateOf(reflection?.wins ?: "") }
    var challenges by remember(reflection) { mutableStateOf(reflection?.challenges ?: "") }
    var gratitude by remember(reflection) { mutableStateOf(reflection?.gratitude ?: "") }
    var intention by remember(reflection) { mutableStateOf(reflection?.tomorrowIntention ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Reflection",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Close the day with clarity. What moved the needle?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = wins,
            onValueChange = { wins = it },
            label = { Text("Wins & progress") },
            placeholder = { Text("What went well today?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = challenges,
            onValueChange = { challenges = it },
            label = { Text("Challenges") },
            placeholder = { Text("What was hard or unexpected?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        OutlinedTextField(
            value = gratitude,
            onValueChange = { gratitude = it },
            label = { Text("Gratitude") },
            placeholder = { Text("Three things you're grateful for…") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        OutlinedTextField(
            value = intention,
            onValueChange = { intention = it },
            label = { Text("Tomorrow's intention") },
            placeholder = { Text("One clear focus for tomorrow") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.saveReflection(
                    energyMorning = reflection?.energyMorning,
                    energyAfternoon = reflection?.energyAfternoon,
                    energyEvening = reflection?.energyEvening,
                    wins = wins,
                    challenges = challenges,
                    gratitude = gratitude,
                    intention = intention
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Reflection")
        }

        Text(
            text = "Reflections are stored locally and help you spot energy patterns over time.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
