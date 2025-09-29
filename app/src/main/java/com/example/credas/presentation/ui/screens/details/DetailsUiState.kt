package com.example.credas.presentation.ui.screens.details

import com.example.credas.domain.model.FoodDetails

data class DetailsUiState(
    val isLoading: Boolean = false,
    val food: FoodDetails? = null,
    val error: String = ""
)