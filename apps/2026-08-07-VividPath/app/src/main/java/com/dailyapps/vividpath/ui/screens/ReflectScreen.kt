package com.dailyapps.vividpath.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.vividpath.ui.components.MoodEnergySelector
import com.dailyapps.vividpath.ui.components.SectionHeader
import com.dailyapps.vividpath.viewmodel.VividViewModel

@Composable
fun ReflectScreen(viewModel: VividViewModel) {
    val existing by viewModel.reflection.collectAsState()

    var mood by remember(existing) { mutableIntStateOf(existing?.mood ?: 3) }
    var energy by remember(existing) { mutableIntStateOf(existing?.energy ?: 3) }
    var wins by remember(existing) { mutableStateOf(existing?.wins ?: "") }
    var lessons by remember(existing) { mutableStateOf(existing?.lessons ?: "") }
    var gratitude by remember(existing) { mutableStateOf(existing?.gratitude ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        SectionHeader(
            title = "Evening Reflection",
            subtitle = "Close the day with clarity and kindness"
        )

        Spacer(modifier = Modifier.height(16.dp))

        MoodEnergySelector(
            label = "Mood (1-5)",
            value = mood,
            onValueChange = { mood = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        MoodEnergySelector(
            label = "Energy (1-5)",
            value = energy,
            onValueChange = { energy = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = wins,
            onValueChange = { wins = it },
            label = { Text("Wins of the day") },
            placeholder = { Text("What went well?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lessons,
            onValueChange = { lessons = it },
            label = { Text("Lessons & friction") },
            placeholder = { Text("What would you do differently?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = gratitude,
            onValueChange = { gratitude = it },
            label = { Text("Gratitude") },
            placeholder = { Text("One thing you are grateful for") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveReflection(mood, energy, wins, lessons, gratitude)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (existing != null) "Update Reflection" else "Save Reflection")
        }

        if (existing != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Reflection saved for today. You can update it anytime.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
