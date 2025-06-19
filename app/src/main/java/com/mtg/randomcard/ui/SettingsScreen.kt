package com.mtg.randomcard.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mtg.randomcard.viewmodel.RandomCardViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(vm: RandomCardViewModel, onBack: () -> Unit) {
    val dark = vm.darkMode.collectAsState().value

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
        }, title = { Text("Settings") })
    }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.animateContentSize()) {
                Text("Dark Mode")
                Spacer(Modifier.weight(1f))
                Switch(checked = dark, onCheckedChange = { vm.toggleDarkMode() })
            }
            Spacer(Modifier.height(24.dp))
            Text("Card images and data courtesy Scryfall.com", style = MaterialTheme.typography.bodySmall)
        }
    }
}