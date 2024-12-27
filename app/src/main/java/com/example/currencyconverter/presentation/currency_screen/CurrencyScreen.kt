package com.example.currencyconverter.presentation.currency_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    var baseCurrencyExpanded by remember { mutableStateOf(false) }
    var targetCurrencyExpanded by remember { mutableStateOf(false) }
    val conversionRate by viewModel.conversionRate.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrencyList()
    }

    when(uiState) {
        is CurrencyUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is CurrencyUiState.Error -> {
            ErrorScreen(
                errorMsg = (uiState as CurrencyUiState.Error).errorMessage
            )
        }
        is CurrencyUiState.Success -> {
            val currencies = (uiState as CurrencyUiState.Success).currencyList
            val baseCurrency = viewModel.baseCurrency.collectAsState().value
            val targetCurrency = viewModel.targetCurrency.collectAsState().value
            val totalAmount by viewModel.totalAmount.collectAsState()
            var inputAmt by remember { mutableStateOf("") }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Convert From: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            CurrencyList(
                                currencies = currencies,
                                expanded = baseCurrencyExpanded,
                                modifier = Modifier
                                    .padding(8.dp),
                                selectedCurrency = baseCurrency,
                                onCurrencySelected = { viewModel.onBaseCurrencySelected(it) },
                                onExpandChange = { baseCurrencyExpanded = !baseCurrencyExpanded }
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Convert To: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            CurrencyList(
                                currencies = currencies,
                                expanded = targetCurrencyExpanded,
                                modifier = Modifier
                                    .padding(8.dp),
                                selectedCurrency = targetCurrency,
                                onCurrencySelected = { viewModel.onTargetCurrencySelected(it) },
                                onExpandChange = {
                                    targetCurrencyExpanded = !targetCurrencyExpanded
                                }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = inputAmt,
                        onValueChange = { inputAmt = it },
                        label = { Text(
                            text = "Enter amount...",
                            fontSize = 14.sp
                        ) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.fetchSpecificRateAndCalculate(baseCurrency, targetCurrency, inputAmt)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Convert")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if(conversionRate != null) {
                        Text(
                            text = "Conversion Rate: $baseCurrency to $targetCurrency = $conversionRate" ,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if(totalAmount != null) {
                        Text(
                            text = "Total amount: ${totalAmount!!.roundToInt()}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    errorMsg?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        CurrencyUiState.Empty -> EmptyScreen()
    }

}

@Composable
fun CurrencyList(
    currencies: Map<String, String>,
    modifier: Modifier = Modifier,
    expanded: Boolean,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit
) {


    Box(modifier = modifier) {
        DropDownList(
            currencies = currencies,
            onCurrencySelected = onCurrencySelected,
            expanded = expanded,
            onExpandChange = onExpandChange,
            selectedCurrency = selectedCurrency
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
    currencies: Map<String, String>,
    expanded: Boolean,
    selectedCurrency: String,
    onExpandChange: (Boolean) -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandChange(!expanded) },
    ) {
        TextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = !expanded)
            },
            modifier = Modifier
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        onCurrencySelected(currency.key)
                        onExpandChange(false)
                    },
                    text = { Text(currency.key) }
                )
            }
        }
    }
}







