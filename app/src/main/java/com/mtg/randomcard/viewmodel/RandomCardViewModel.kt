package com.mtg.randomcard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mtg.randomcard.network.NetworkModule
import com.mtg.randomcard.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val RATE_LIMIT_MS = 150L // Scryfall: ≤10 req/s

class RandomCardViewModel : ViewModel() {
    // ===== Theme =====
    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()
    fun toggleDarkMode() { _darkMode.value = !_darkMode.value }

    // ===== Card state =====
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val history = mutableListOf<com.mtg.randomcard.data.CardDto>()

    init { fetch() }

    fun fetch() = viewModelScope.launch {
        _state.value = UiState.Loading
        delay(RATE_LIMIT_MS)
        runCatching { NetworkModule.api.randomCard() }
            .onSuccess { card ->
                history.add(0, card)
                _state.value = UiState.Success(card, history.toList())
            }
            .onFailure { _state.value = UiState.Error(it.localizedMessage ?: "Timeout") }
    }

    fun removeCard(card: com.mtg.randomcard.data.CardDto) {
        history.remove(card)
        val current = history.firstOrNull()
        _state.value = current?.let { UiState.Success(it, history.toList()) }
            ?: UiState.Error("History empty – tap refresh")
    }

    fun clearHistory() {
        history.clear()
        _state.value = UiState.Error("History cleared – draw again")
    }

    fun cardAt(index: Int): com.mtg.randomcard.data.CardDto? = history.getOrNull(index)
}