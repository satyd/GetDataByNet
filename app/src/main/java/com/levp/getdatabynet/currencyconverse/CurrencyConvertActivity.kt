package com.levp.getdatabynet.currencyconverse

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.levp.getdatabynet.currencyconverse.repository.CCViewModel
import com.levp.getdatabynet.databinding.ActivityCurrencyConverseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CurrencyConvertActivity : AppCompatActivity() {

    private val viewModel: CCViewModel by viewModels()
    private lateinit var binding: ActivityCurrencyConverseBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyConverseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConvert.setOnClickListener {
            viewModel.convert(
                binding.etFrom.text.toString(),
                binding.spFromCurrency.selectedItem.toString(),
                binding.spToCurrency.selectedItem.toString(),
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when(event) {
                    is CCViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultText
                    }
                    is CCViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText
                    }
                    is CCViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

}