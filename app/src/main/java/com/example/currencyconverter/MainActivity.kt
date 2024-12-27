package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyconverter.presentation.currency_screen.CurrencyScreen
import com.example.currencyconverter.presentation.currency_screen.CurrencyViewModel
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: CurrencyViewModel = viewModel(factory = CurrencyViewModel.factory)
                    CurrencyScreen(
                        modifier = Modifier
                            .padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

