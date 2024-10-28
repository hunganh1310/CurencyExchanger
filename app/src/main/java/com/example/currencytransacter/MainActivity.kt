package com.example.currencytransacter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var sourceAmount: EditText
    private lateinit var destinationAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var destinationCurrency: Spinner

    // Exchange rate for USD to VND and vice versa
    private val usdToVndRate = 23000.0
    private val currencies = listOf("USD", "VND")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmount = findViewById(R.id.sourceAmount)
        destinationAmount = findViewById(R.id.destinationAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        destinationCurrency = findViewById(R.id.destinationCurrency)

        // Set up the currency spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceCurrency.adapter = adapter
        destinationCurrency.adapter = adapter

        // Set up listeners for automatic updates
        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateConversion()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        destinationCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Focus change listeners to determine source and destination
        sourceAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setSourceField(sourceAmount, sourceCurrency)
                setDestinationField(destinationAmount, destinationCurrency)
            }
        }

        destinationAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setSourceField(destinationAmount, destinationCurrency)
                setDestinationField(sourceAmount, sourceCurrency)
            }
        }
    }

    private fun setSourceField(amountField: EditText, currencyField: Spinner) {
        amountField.isEnabled = true
        currencyField.isEnabled = true
    }

    private fun setDestinationField(amountField: EditText, currencyField: Spinner) {
        amountField.isEnabled = false
        currencyField.isEnabled = false
    }

    // Đảm bảo khai báo usdToVndRate là kiểu Double

    private fun updateConversion() {
        val sourceCurrencyCode = sourceCurrency.selectedItem.toString()
        val destinationCurrencyCode = destinationCurrency.selectedItem.toString()
        val sourceValue = sourceAmount.text.toString().toDoubleOrNull() ?: return // Ép kiểu về Double

        val result = if (sourceCurrencyCode == "USD" && destinationCurrencyCode == "VND") {
            sourceValue * usdToVndRate // usdToVndRate là Double
        } else if (sourceCurrencyCode == "VND" && destinationCurrencyCode == "USD") {
            sourceValue / usdToVndRate // usdToVndRate là Double
        } else {
            sourceValue
        }

        destinationAmount.setText((round(result * 100) / 100).toString())
    }


}
