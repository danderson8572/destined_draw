package com.mtg.randomcard.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.mtg.randomcard.viewmodel.RandomCardViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import com.mtg.randomcard.ui.CardDisplay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RandomCardScreen(
    vm: RandomCardViewModel,
    onHistory: () -> Unit,
    onSettings: () -> Unit
) {
    val ui = vm.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Destined Draw") }, actions = {
                IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, "Settings") }
                IconButton(onClick = onHistory) { Icon(Icons.Default.History, "History") }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.draw() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New card")
            }
        }
    ) { pad ->
        AnimatedContent(targetState = ui, label = "CardSwap") { state ->
            when (state) {
                UiState.Loading  -> CenterBox(pad) { CircularProgressIndicator() }
                is UiState.Error -> CenterBox(pad) { Text(state.message) }
                is UiState.Success -> CardDisplay(state.current, Modifier.padding(pad))
            }
        }
    }
}