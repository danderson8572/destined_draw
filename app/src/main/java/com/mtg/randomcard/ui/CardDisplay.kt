package com.mtg.randomcard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mtg.randomcard.data.CardDto

@Composable
fun CardDisplay(card: CardDto, modifier: Modifier = Modifier) {
    val face = card.card_faces?.firstOrNull()
    val image = face?.image_uris?.normal ?: card.image_uris?.normal
    val name  = face?.name ?: card.name
    val text  = face?.oracle_text ?: card.oracle_text
    val type  = face?.type_line ?: card.type_line
    val stats = when {
        face?.power != null && face.toughness != null -> "${face.power}/${face.toughness}"
        card.power != null && card.toughness != null   -> "${card.power}/${card.toughness}"
        else                                           -> null
    }

    Column(modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(model = image, contentDescription = name, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Text(name, style = MaterialTheme.typography.headlineSmall)
        type?.let  { Text(it, style = MaterialTheme.typography.bodyMedium) }
        stats?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        Spacer(Modifier.height(8.dp))
        text?.let  { Text(it, style = MaterialTheme.typography.bodyLarge) }
    }
}