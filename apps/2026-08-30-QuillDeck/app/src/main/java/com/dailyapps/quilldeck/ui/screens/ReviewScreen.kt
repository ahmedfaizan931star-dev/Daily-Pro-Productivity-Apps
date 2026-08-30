package com.dailyapps.quilldeck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun ReviewScreen(vm: QuillViewModel) {
    val state by vm.ui.collectAsStateWithLifecycle()
    val card = state.due.firstOrNull()
    var flipped by remember(card?.id) { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Review", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        if (card == null) {
            Text("Queue clear. Add cards in Decks or come back tomorrow.")
        } else {
            Card(Modifier.fillMaxWidth().clickable { flipped = !flipped }) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(if (flipped) "Answer" else "Prompt", style = MaterialTheme.typography.labelLarge)
                    Text(if (flipped) card.back else card.front, style = MaterialTheme.typography.titleLarge)
                    Text("Tap to flip", style = MaterialTheme.typography.bodySmall)
                }
            }
            if (flipped) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { vm.review(card, 1); flipped = false }, modifier = Modifier.weight(1f)) {
                        Text("Again")
                    }
                    Button(onClick = { vm.review(card, 3); flipped = false }, modifier = Modifier.weight(1f)) {
                        Text("Good")
                    }
                    Button(onClick = { vm.review(card, 5); flipped = false }, modifier = Modifier.weight(1f)) {
                        Text("Easy")
                    }
                }
            }
        }
    }
}
