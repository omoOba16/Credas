package com.example.credas

import com.example.credas.data.mapper.toFoodDetails
import com.example.credas.data.mapper.toMenu
import com.example.credas.data.model.FoodDto
import com.example.credas.data.model.MenuDtoItem
import com.example.credas.data.model.NetworkState
import com.example.credas.data.repositories.MenuRepositoryImp
import com.example.credas.data.source.MenuRemoteDataSource
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MenuRepositoryImpTest {

    @get:Rule
    val mockRule = MockKRule(this)

    @MockK
    private lateinit var menuRemoteDataSource: MenuRemoteDataSource

    private lateinit var menuRepositoryImp: MenuRepositoryImp

    @Before
    fun setUp() {
        menuRepositoryImp = MenuRepositoryImp(menuRemoteDataSource)
    }

    // Test cases for Menu

    @Test
    fun `test getMenu returns success with mapped foods`() = runTest {
        val menuDto = listOf(
            MenuDtoItem("Starter", 1, "Chicken Wings", 12.50),
            MenuDtoItem("Main", 2, "Stake", 25.50),
        )

        val expectedDomainFoods = menuDto.toMenu()

        val mockResponse: Response<List<MenuDtoItem?>> = Response.success(menuDto)

        coEvery { menuRemoteDataSource.getMenu() } returns mockResponse

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Success)
        assertEquals(expectedDomainFoods, (result as NetworkState.Success).data)
    }

    @Test
    fun `test getMenu returns success with empty list when API response body is null`() = runTest {
        val mockResponse: Response<List<MenuDtoItem?>> = Response.success(null)
        coEvery { menuRemoteDataSource.getMenu() } returns mockResponse

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Success)
        assertTrue("Expected empty list for null body",
            (result as NetworkState.Success).data?.isEmpty() == true
        )
    }

    // Test cases for Food
    @Test
    fun `test getFood returns success with mapped food on successful API call`() = runTest {
        val foodId = 1
        val foodDto =
            FoodDto("Main", "Delicious cheese burger", foodId, "", "Cheese Burger", 10.50)
        val expectedDomainFood = foodDto.toFoodDetails()
        val mockResponse: Response<FoodDto?> = Response.success(foodDto)

        coEvery { menuRemoteDataSource.getFood(foodId) } returns mockResponse

        val result = menuRepositoryImp.getFood(foodId)

        assertTrue(result is NetworkState.Success)
        assertEquals(expectedDomainFood, (result as NetworkState.Success).data)
    }

    // Test cases for Error Handling
    @Test
    fun `test returns Error on SocketTimeoutException`() = runTest {
        coEvery { menuRemoteDataSource.getMenu() } throws SocketTimeoutException()

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Error)
        assertEquals("Timeout Error: Please check your connection.", (result as NetworkState.Error).message)
    }

    @Test
    fun `test returns Error on UnknownHostException`() = runTest {
        coEvery { menuRemoteDataSource.getMenu() } throws UnknownHostException()

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Error)
        assertEquals("Network Error: Check your internet connection.", (result as NetworkState.Error).message)
    }

    @Test
    fun `test returns Error on IOException`() = runTest {
        coEvery { menuRemoteDataSource.getMenu() } throws IOException()

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Error)
        assertEquals("Network Error: An I/O error occurred.", (result as NetworkState.Error).message)
    }

    @Test
    fun `test returns Error on Generic Exception`() = runTest {
        coEvery { menuRemoteDataSource.getMenu() } throws RuntimeException()

        val result = menuRepositoryImp.getMenu()

        assertTrue(result is NetworkState.Error)
        assertEquals("An unknown error occurred.", (result as NetworkState.Error).message)
    }
}