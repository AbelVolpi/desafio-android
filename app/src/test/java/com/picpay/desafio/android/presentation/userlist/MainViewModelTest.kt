package com.picpay.desafio.android.presentation.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UsersRepository
import com.picpay.desafio.android.presentation.utils.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var usersRepository: UsersRepository

    private lateinit var mainViewModel: MainViewModel

    @MockK(relaxed = true)
    lateinit var observer: Observer<UiState<List<User>>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        mainViewModel = MainViewModel(usersRepository)
        mainViewModel.uiState.observeForever(observer)
    }

    @Test
    fun `fetchUsers should post Success when repository returns data`() = runTest {
        // Given
        val users = listOf(User("image_url", "Name1", 0, "User1"))
        coEvery { usersRepository.getUsers() } returns users

        // When
        mainViewModel.fetchUsers()
        advanceUntilIdle()

        // Then
        verifySequence {
            observer.onChanged(UiState.Loading)
            observer.onChanged(UiState.Success(users))
        }
    }

    @Test
    fun `fetchUsers should post Failure when repository throws exception`() = runTest {
        // Given
        val exception = IOException("Network error")
        coEvery { usersRepository.getUsers() } throws exception

        // When
        mainViewModel.fetchUsers()
        advanceUntilIdle()

        // Then
        verifySequence {
            observer.onChanged(UiState.Loading)
            observer.onChanged(UiState.Failure(exception))
        }
    }
}
