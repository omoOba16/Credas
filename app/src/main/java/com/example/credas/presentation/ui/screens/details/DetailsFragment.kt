package com.example.credas.presentation.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.load
import com.example.credas.databinding.FragmentDetailsBinding
import com.example.credas.presentation.viewModel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class DetailsFragment(): Fragment() {

    private val viewModel: MenuViewModel by viewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.detailsUiState.collect { data ->
                    updateUi(data)
                }
            }
        }
    }

    private fun updateUi(state: DetailsUiState){
        if(state.isLoading){
            binding.loadingProgressBar.visibility = View.VISIBLE
        }

        if(state.food != null){
            binding.loadingProgressBar.visibility = View.GONE
            binding.food = state.food
            binding.imageView.load(state.food.image)
        }

        if(state.error.isNotBlank()){
            binding.loadingProgressBar.visibility = View.GONE
            binding.emptyTextView.visibility = View.VISIBLE
            binding.emptyTextView.text = state.error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}