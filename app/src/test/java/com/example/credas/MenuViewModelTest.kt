package com.example.credas

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.credas.data.model.NetworkState
import com.example.credas.domain.model.Food
import com.example.credas.domain.model.GroupedFood
import com.example.credas.domain.useCases.GetFoodDetailsUseCase
import com.example.credas.domain.useCases.GetMenuUseCase
import com.example.credas.presentation.viewModel.MenuViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MenuViewModelTest {


    @get:Rule
    val mockkRule = MockKRule(this) // Initializes @MockK annotated fields

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var getMenuUseCase: GetMenuUseCase

    @MockK
    private lateinit var getFoodDetailsUseCase: GetFoodDetailsUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var menuViewModel: MenuViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set Main dispatcher for ViewModelScope
        savedStateHandle =
            SavedStateHandle() // Initialize with an empty state or pre-fill if needed
        menuViewModel = MenuViewModel(
            getFoodDetailsUseCase,
            getMenuUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `getMenu updates menuUiState to Success with grouped foods on successful UseCase call`() = runTest {
        val foodList = listOf(
            Food("Main", 3, "Cheese pizza", "£14", 14.0),
            Food("Dessert", 2, "Toffee Cake", "£14.50", 14.50),
        )

        val expectedGroupedFoods = listOf(
            GroupedFood.Category("Main"),
            GroupedFood.Foods(foodList[0]),
            GroupedFood.Category("Dessert"),
            GroupedFood.Foods(foodList[1])
        )
        coEvery { getMenuUseCase() } returns NetworkState.Success(expectedGroupedFoods)

        menuViewModel.menuUiState.test {

            menuViewModel.getMenu() // Manually trigger getMenu

            val loadingState = awaitItem() // State update: isLoading = true
            Assert.assertTrue("Menu should be loading", loadingState.isLoading)

            val successState = awaitItem() // State update: Success
            Assert.assertFalse("Menu should not be loading after success", successState.isLoading)
            Assert.assertEquals(expectedGroupedFoods, successState.foods)
            Assert.assertNull("Error should be null on success", successState.error)

            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { getMenuUseCase() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after the test
    }
}