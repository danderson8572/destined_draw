package com.mtg.randomcard.ui

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mtg.randomcard.data.CardDto
import com.mtg.randomcard.ui.CardDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(card: CardDto) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(card.name) })
    }) { pad ->
        CardDisplay(card, Modifier.padding(pad))
    }
}