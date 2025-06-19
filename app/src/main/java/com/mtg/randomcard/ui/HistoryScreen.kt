package com.mtg.randomcard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mtg.randomcard.data.CardDto
import com.mtg.randomcard.viewmodel.RandomCardViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HistoryScreen(vm: RandomCardViewModel, onBack: () -> Unit, onOpen: (Int) -> Unit) {
    val ui = vm.state.collectAsState().value
    val history = (ui as? UiState.Success)?.history ?: emptyList()

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
        }, title = { Text("History") }, actions = {
            IconButton(onClick = vm::clearHistory) { Icon(Icons.Default.Delete, "Clear") }
        })
    }) { pad ->
        LazyColumn(Modifier.padding(pad)) {
            itemsIndexed(history, key = { _, c -> c.name }) { idx, card ->
                AnimatedVisibility(visible = true, modifier = Modifier.animateItemPlacement(), label = "RowAnim") {
                    CardRow(card, onClick = { onOpen(idx) }, onRemove = { vm.removeCard(card) })
                }
            }
        }
    }
}

@Composable
private fun CardRow(card: CardDto, onClick: () -> Unit, onRemove: () -> Unit) {
    Card(shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .animateContentSize()) {
        Row(Modifier.clickable { onClick() }.padding(8.dp)) {
            AsyncImage(model = card.image_uris?.normal, contentDescription = card.name, modifier = Modifier.size(80.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(card.name, style = MaterialTheme.typography.bodyLarge)
                card.type_line?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, "Del") }
        }
    }
}