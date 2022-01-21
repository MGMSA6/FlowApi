package com.example.flowapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flowapi.ViewModel.MainViewModel
import com.example.flowapi.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btnLiveData.setOnClickListener {
            mainViewModel.triggerLiveData()
        }

        binding.btnStateFlow.setOnClickListener {
            mainViewModel.triggerStateFlow()
        }

        binding.btnSharedFlow.setOnClickListener {
            mainViewModel.triggerSharedFlow()
        }

        binding.btnFlow.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.triggerFlow().collect {
                    binding.txtFlow.text = it
                }
            }
        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        mainViewModel.liveData.observe(this, Observer {
            binding.txtLiveData.text = it
        })

        lifecycleScope.launchWhenStarted {
            mainViewModel.stateFlow.collectLatest {
                binding.txtStateFlow.text = it
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.sharedFlow.collectLatest {
                binding.txtSharedFlow.text = it
            }
        }

    }
}