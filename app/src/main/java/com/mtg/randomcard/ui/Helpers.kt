package com.mtg.randomcard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterBox(padding: PaddingValues, content: @Composable () -> Unit) {
    Box(Modifier.padding(padding).fillMaxSize(), Alignment.Center) { content() }
}