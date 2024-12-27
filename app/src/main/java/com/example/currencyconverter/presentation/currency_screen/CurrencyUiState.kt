package com.example.currencyconverter.presentation.currency_screen

sealed class CurrencyUiState {
    data class Success(val currencyList: Map<String, String>): CurrencyUiState()
    object Loading: CurrencyUiState()
    data class Error(val errorMessage: String): CurrencyUiState()
    object Empty: CurrencyUiState()
}