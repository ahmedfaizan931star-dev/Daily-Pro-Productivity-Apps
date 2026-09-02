package com.dailyapps.vespergate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.vespergate.data.LoopEntity

@Composable
fun LoopsScreen(
    loops: List<LoopEntity>,
    onAdd: (String) -> Unit,
    onPark: (LoopEntity) -> Unit,
    onClose: (LoopEntity) -> Unit,
    onDelete: (LoopEntity) -> Unit
) {
    var draft by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Open loops", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Name the thread so it does not follow you into sleep.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Unfinished thread") }
        )
        OutlinedButton(onClick = {
            onAdd(draft)
            draft = ""
        }) { Text("Capture") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(loops, key = { it.id }) { loop ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(loop.title, style = MaterialTheme.typography.titleMedium)
                        Text(loop.status.uppercase(), style = MaterialTheme.typography.labelMedium)
                        Row {
                            TextButton(onClick = { onPark(loop) }) { Text("Park") }
                            TextButton(onClick = { onClose(loop) }) { Text("Close") }
                            TextButton(onClick = { onDelete(loop) }) { Text("Drop") }
                        }
                    }
                }
            }
        }
    }
}
