package com.dailyapps.quilldeck.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyapps.quilldeck.viewmodel.QuillViewModel

@Composable
fun DecksScreen(vm: QuillViewModel) {
    val state by vm.ui.collectAsStateWithLifecycle()
    var deckName by remember { mutableStateOf("") }
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }
    val selected = state.decks.firstOrNull()
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Decks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(deckName, { deckName = it }, label = { Text("New deck") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { vm.addDeck(deckName); deckName = "" }, enabled = deckName.isNotBlank()) {
            Text("Create deck")
        }
        if (selected != null) {
            Text("Add card to ${selected.name}", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(front, { front = it }, label = { Text("Front") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(back, { back = it }, label = { Text("Back") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = { vm.addCard(selected.id, front, back); front = ""; back = "" },
                enabled = front.isNotBlank() && back.isNotBlank()
            ) { Text("Save card") }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.decks, key = { it.id }) { deck ->
                val count = state.cards.count { it.deckId == deck.id }
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(deck.name, fontWeight = FontWeight.SemiBold)
                        Text("$count cards", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
