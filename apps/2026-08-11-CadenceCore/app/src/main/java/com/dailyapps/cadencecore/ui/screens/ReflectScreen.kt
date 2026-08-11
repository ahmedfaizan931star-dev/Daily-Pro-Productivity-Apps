package com.dailyapps.cadencecore.ui.screens

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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.cadencecore.ui.components.MoodSelector
import com.dailyapps.cadencecore.ui.components.SectionHeader
import com.dailyapps.cadencecore.viewmodel.CadenceViewModel

@Composable
fun ReflectScreen(viewModel: CadenceViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        SectionHeader(
            title = "Reflect",
            subtitle = "Close the day with honesty and clarity"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How was your mood?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        MoodSelector(
            selected = state.mood,
            onSelect = { viewModel.setMood(it) }
        )

        // Make mood selectable by clicking labels
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap an emoji above or use the slider",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Slider(
            value = state.mood.toFloat(),
            onValueChange = { viewModel.setMood(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Energy level: ${state.energy}/5",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Slider(
            value = state.energy.toFloat(),
            onValueChange = { viewModel.setEnergy(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Journal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.reflectionText,
            onValueChange = { viewModel.setReflectionText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            placeholder = { Text("What went well? What will you improve tomorrow?") },
            maxLines = 8
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveReflection() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save reflection")
        }

        if (state.reflection != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Saved for today",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
