package com.mtg.randomcard.ui

import com.mtg.randomcard.data.CardDto

sealed interface UiState {
    object Loading : UiState
    data class Success(
        val current: CardDto,
        val history: List<CardDto>
    ) : UiState
    data class Error(val message: String) : UiState
}