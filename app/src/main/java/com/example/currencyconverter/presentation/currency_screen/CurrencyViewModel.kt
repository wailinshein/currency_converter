package com.example.currencyconverter.presentation.currency_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.currencyconverter.CurrencyConverterApplication
import com.example.currencyconverter.domain.repository.CacheRepository
import com.example.currencyconverter.domain.repository.CurrencyConverterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(
    val currencyConverterRepository: CurrencyConverterRepository,
    val offlineRepositoryImpl: CurrencyConverterRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<CurrencyUiState>(CurrencyUiState.Loading)
    val uiState: StateFlow<CurrencyUiState> = _uiState

    private val _baseCurrency = MutableStateFlow("Choose..")
    val baseCurrency: StateFlow<String> = _baseCurrency

    private val _targetCurrency = MutableStateFlow("Choose..")
    val targetCurrency: StateFlow<String> = _targetCurrency

    private val _conversionRate = MutableStateFlow<Double?>(null)
    val conversionRate: StateFlow<Double?> = _conversionRate

    private val _totalAmount = MutableStateFlow<Double?>(null)
    val totalAmount: StateFlow<Double?> = _totalAmount

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    fun fetchCurrencyList() {
        viewModelScope.launch {
            _uiState.value = CurrencyUiState.Loading
            try {
                val currencies = currencyConverterRepository.getCurrencyList()
                if(currencies.isNotEmpty()) {
                    _uiState.value = CurrencyUiState.Success(currencies)
                } else {
                    _uiState.value = CurrencyUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = CurrencyUiState.Error(e.message ?: "Unknown Error occurred")
            } catch (e: HttpException) {
                _uiState.value = CurrencyUiState.Error("No Internet Connection")
            }
        }
    }

    fun onBaseCurrencySelected(currency: String) {
        _baseCurrency.value = currency
    }

    fun onTargetCurrencySelected(currency: String) {
        _targetCurrency.value = currency
    }

    fun fetchSpecificRateAndCalculate(baseCurrency: String, targetCurrency: String, amount: String) {
        viewModelScope.launch {
            try {
                val cachedRate = offlineRepositoryImpl.getSpecificRate(baseCurrency, targetCurrency)
                val rate: Double? = cachedRate ?: currencyConverterRepository.getSpecificRate(baseCurrency, targetCurrency)?.also {
                    (offlineRepositoryImpl as? CacheRepository)?.saveRate(baseCurrency, targetCurrency, it)
                }
                Log.d("Fetched exchange rates", "$rate")
                _conversionRate.value = rate
                val inputAmt = amount.toDoubleOrNull()
                if(rate != null && inputAmt != null) {
                    _totalAmount.value = inputAmt * rate
                } else {
                    _totalAmount.value = null
                }
            } catch (e: HttpException) {
                Log.e("Error", "An error occurred", e)
                _errorMsg.value = "Unknown error occurred"
            } catch(e: Exception) {
                _errorMsg.value = "Unknown error occurred"
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CurrencyConverterApplication)
                val currencyConverterRepository = application.container.currencyConverterRepository
                val offlineRepositoryImpl = application.container.offlineRepository
                CurrencyViewModel(
                    currencyConverterRepository = currencyConverterRepository,
                    offlineRepositoryImpl = offlineRepositoryImpl
                )
            }
        }
    }
}
