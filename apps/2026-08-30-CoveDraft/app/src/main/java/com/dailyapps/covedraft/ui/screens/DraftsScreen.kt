package com.dailyapps.covedraft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.covedraft.viewmodel.CoveViewModel

@Composable
fun DraftsScreen(vm: CoveViewModel) {
    val drafts by vm.drafts.collectAsState()
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Park a draft so it stops occupying working memory.")
        OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(note, { note = it }, label = { Text("Context") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            if (title.isNotBlank()) {
                vm.addDraft(title.trim(), note.trim())
                title = ""
                note = ""
            }
        }) { Text("Park draft") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(drafts, key = { it.id }) { d ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(d.title)
                        if (d.note.isNotBlank()) Text(d.note)
                        Row {
                            TextButton(onClick = { vm.toggleLaunch(d) }) {
                                Text(if (d.launched) "Return to harbor" else "Add to launch list")
                            }
                            TextButton(onClick = { vm.deleteDraft(d.id) }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }
}
