package com.example.credas.presentation.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credas.data.model.NetworkState
import com.example.credas.domain.useCases.GetFoodDetailsUseCase
import com.example.credas.domain.useCases.GetMenuUseCase
import com.example.credas.presentation.ui.screens.details.DetailsUiState
import com.example.credas.presentation.ui.screens.menu.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getFoodDetailsUseCase: GetFoodDetailsUseCase,
    private val getMenuUseCase: GetMenuUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val menuTAG = "MenuTag"
    private val foodTAG = "FoodTag"
    val foodId: Int = savedStateHandle.get<Int>("id") ?: 0

    // Menus
    private val _menuUiState = MutableStateFlow(MenuUiState())
    val menuUiState = _menuUiState
        .onStart { getMenu() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MenuUiState()
        )

    // Food Details
    private val _detailsUiState = MutableStateFlow(DetailsUiState())
    val detailsUiState = _detailsUiState
        .onStart { getFoodDetails(foodId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailsUiState()
        )

    fun getMenu() = viewModelScope.launch {

        Log.d(menuTAG, "Loading...")
        _menuUiState.update {
            it.copy(isLoading = true)
        }

        val response = getMenuUseCase()
        when(response){
            is NetworkState.Success -> {
                Log.d(menuTAG, "Success: ${response.data}")
                _menuUiState.update {
                    it.copy(
                        isLoading = false,
                        foods = response.data
                    )
                }
            }
            is NetworkState.Error -> {
                Log.d(menuTAG, "Error: ${response.message}")
                val error = response.message
                _menuUiState.update {
                    it.copy(
                        isLoading = false,
                        error = error
                    )
                }
            }
        }
    }

    fun getFoodDetails(foodId: Int) = viewModelScope.launch {

        _detailsUiState.update {
            it.copy(isLoading = true)
        }

        val response = getFoodDetailsUseCase(foodId)
        when(response){
            is NetworkState.Success -> {
                Log.d(foodTAG, "Success: ${response.data}")
                _detailsUiState.update {
                    it.copy(
                        isLoading = false,
                        food = response.data
                    )
                }
            }
            is NetworkState.Error -> {
                Log.d(foodTAG, "Error: ${response.message}")
                val error = response.message
                _detailsUiState.update {
                    it.copy(
                        isLoading = false,
                        error = error
                    )
                }
            }
        }
    }
}