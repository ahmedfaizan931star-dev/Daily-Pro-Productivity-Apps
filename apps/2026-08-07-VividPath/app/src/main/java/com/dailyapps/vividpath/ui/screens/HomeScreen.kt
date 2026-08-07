package com.dailyapps.vividpath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyapps.vividpath.ui.components.SectionHeader
import com.dailyapps.vividpath.ui.components.StatChip
import com.dailyapps.vividpath.ui.theme.Amber
import com.dailyapps.vividpath.ui.theme.Emerald
import com.dailyapps.vividpath.ui.theme.SkyBlue
import com.dailyapps.vividpath.viewmodel.VividViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HomeScreen(viewModel: VividViewModel) {
    val state by viewModel.homeState.collectAsState()
    var intentionText by remember(state.intention) { mutableStateOf(state.intention) }

    val todayFormatted = remember {
        LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "VividPath",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = todayFormatted,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            StatChip("Completed", "${state.completedCount}/${state.totalCount}", Emerald)
            StatChip("Focus", "${state.focusMinutes}m", SkyBlue)
            StatChip("Reflect", if (state.hasReflection) "Done" else "Open", Amber)
        }

        Spacer(modifier = Modifier.height(28.dp))

        SectionHeader(
            title = "Today's Intention",
            subtitle = "One clear focus that guides your path"
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text(
                        text = if (state.intention.isBlank()) "Set your guiding intention" else "Your intention",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = intentionText,
                    onValueChange = { intentionText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Ship the proposal and protect deep work") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.setIntention(intentionText) },
                    modifier = Modifier.align(Alignment.End),
                    enabled = intentionText.isNotBlank()
                ) {
                    Text("Save Intention")
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        SectionHeader(
            title = "Path Snapshot",
            subtitle = if (state.pathItems.isEmpty()) "Add steps on the Path tab" else "Your top path items"
        )

        if (state.pathItems.isEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No path items yet. Head to the Path tab to define the steps that move your intention forward.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            state.pathItems.take(4).forEach { item ->
                Text(
                    text = "• ${item.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = if (item.status.name == "DONE")
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            if (state.pathItems.size > 4) {
                Text(
                    text = "+${state.pathItems.size - 4} more on Path tab",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Walk your path with intention. Focus deeply. Reflect honestly.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
