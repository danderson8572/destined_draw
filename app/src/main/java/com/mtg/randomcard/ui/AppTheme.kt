package com.mtg.randomcard.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.mtg.randomcard.viewmodel.RandomCardViewModel

private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260)
)
private val DarkColors  = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8)
)

@Composable
fun AppTheme(vm: RandomCardViewModel, content: @Composable () -> Unit) {
    val dark by vm.darkMode.collectAsState()
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
