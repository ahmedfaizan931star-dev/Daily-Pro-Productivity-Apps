package com.dailyapps.kitebrief.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.kitebrief.data.model.Commitment

@Composable
fun CommitmentsScreen(
    items: List<Commitment>,
    onAdd: (String) -> Unit,
    onToggle: (Commitment) -> Unit,
    onRemove: (Commitment) -> Unit
) {
    var draft by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Three lines of lift", style = MaterialTheme.typography.headlineLarge)
        Text("Keep the kite light. Three commitments is plenty.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            label = { Text("Add a commitment") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onAdd(draft); draft = "" }, enabled = items.size < 5) { Text("Add") }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(items, key = { it.id }) { item ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(checked = item.done, onCheckedChange = { onToggle(item) })
                    Text(item.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { onRemove(item) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}
