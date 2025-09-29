package com.example.credas.presentation.ui.screens.menu

import com.example.credas.domain.model.GroupedFood

data class MenuUiState(
    val isLoading: Boolean = false,
    val foods: List<GroupedFood> = emptyList(),
    val error: String = ""
)