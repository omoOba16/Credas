package com.example.credas.presentation.ui.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.credas.databinding.FragmentMenuBinding
import com.example.credas.presentation.ui.screens.menu.MenuUiState
import com.example.credas.presentation.ui.adapter.FoodAdapter
import com.example.credas.presentation.viewModel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment(): Fragment() {
    private val viewModel: MenuViewModel by viewModels()

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FoodAdapter { food ->
            val action = MenuFragmentDirections.actionMenuFragmentToDetailFragment(food.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.menuUiState.collect { data ->
                    updateUi(data)
                }
            }
        }
    }

    private fun updateUi(state: MenuUiState){
        if(state.isLoading){
            binding.loadingProgressBar.visibility = View.VISIBLE
        }

        if(state.foods.isNotEmpty()){
            binding.loadingProgressBar.visibility = View.GONE
            adapter.setFoods(state.foods)
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