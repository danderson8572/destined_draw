package com.mtg.randomcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mtg.randomcard.data.CardDto
import com.mtg.randomcard.data.HistoryStore
import com.mtg.randomcard.network.NetworkModule
import com.mtg.randomcard.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val RATE_LIMIT_MS = 150L
private const val QUEUE_SIZE = 5

class RandomCardViewModel(app: Application) : AndroidViewModel(app) {
    // Theme ---------------------------------------------------------------
    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()
    fun toggleDarkMode() { _darkMode.value = !_darkMode.value }

    // History -------------------------------------------------------------
    private val _history = MutableStateFlow<List<CardDto>>(emptyList())
    val history: StateFlow<List<CardDto>> = _history.asStateFlow()

    // Prefetch queue ------------------------------------------------------
    private val queue = ArrayDeque<CardDto>()

    // UI state ------------------------------------------------------------
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        loadHistory()
        viewModelScope.launch { prefillQueue() }
    }

    fun draw() {
        if (queue.isEmpty()) return  // fallback safeguard
        val card = queue.removeFirst()
        _history.update { listOf(card) + it }
        _state.value = UiState.Success(card, _history.value)
        persist()
        viewModelScope.launch { prefillQueue() }
    }

    fun removeCard(card: CardDto) { _history.update { it - card }; persist() }
    fun clearHistory() { _history.value = emptyList(); _state.value = UiState.Error("History cleared – draw again"); persist() }
    fun cardAt(idx: Int) = _history.value.getOrNull(idx)

    // ---------------- helpers ----------------
    private fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            HistoryStore.flow(getApplication()).collect { saved ->
                _history.value = saved
                if (saved.isNotEmpty()) _state.value = UiState.Success(saved.first(), saved)
            }
        }
    }

    private fun persist() = viewModelScope.launch(Dispatchers.IO) { HistoryStore.save(getApplication(), _history.value) }

    private suspend fun prefillQueue() {
        while (queue.size < QUEUE_SIZE) {
            runCatching { NetworkModule.api.randomCard() }
                .onSuccess { queue.addLast(it) }
            delay(RATE_LIMIT_MS)
        }
    }
}