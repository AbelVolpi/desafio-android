package com.picpay.desafio.android.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.entity.toEntity
import com.picpay.desafio.android.data.remote.model.UserResponse
import com.picpay.desafio.android.data.remote.model.toDomain
import com.picpay.desafio.android.data.remote.service.PicPayService
import com.picpay.desafio.android.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UsersRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    lateinit var picPayService: PicPayService

    @MockK(relaxed = true)
    private lateinit var userDao: UserDao

    private lateinit var usersRepository: UsersRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        usersRepository = UsersRepositoryImpl(picPayService, userDao, testDispatcher)
    }

    @Test
    fun `getUsers should return list of users on success`() = runTest {
        // Given
        val usersResponse = listOf(UserResponse("image_url", "Name1", 0, "User1"))
        coEvery { picPayService.getUsers() } returns usersResponse

        // When
        val result = usersRepository.getUsers()

        // Then
        assertEquals(usersResponse.toDomain(), result)
        coVerify(exactly = 1) { picPayService.getUsers() }
    }

    @Test
    fun `getUsers should throw exception on failure`() = runTest {
        // Given
        coEvery { picPayService.getUsers() } throws IOException("Network error")

        // When
        val result = try {
            usersRepository.getUsers()
        } catch (error: Exception) {
            error
        }

        // Then
        assert(result is IOException)
        assertEquals("Network error", (result as IOException).message)
    }

    @Test
    fun `getUsers should return cached users if available`() = runTest {
        // Given
        val cachedUsers = listOf(User("image_url", "Name1", 0, "User1"))
        coEvery { userDao.getUsers() } returns cachedUsers.toEntity()

        // When
        val result = usersRepository.getUsers()

        // Then
        assertEquals(cachedUsers, result)
        coVerify(exactly = 1) { userDao.getUsers() }
        coVerify(exactly = 0) { picPayService.getUsers() }
    }

    @Test
    fun `getUsers should fetch from API and save in cache when no local data exists`() = runTest {
        // Given
        coEvery { userDao.getUsers() } returns emptyList()
        val usersResponse = listOf(UserResponse("image_url", "Name1", 0, "User1"))
        coEvery { picPayService.getUsers() } returns usersResponse

        // When
        val result = usersRepository.getUsers()

        // Then
        assertEquals(usersResponse.toDomain(), result)
        coVerify(exactly = 1) { userDao.getUsers() }
        coVerify(exactly = 1) { picPayService.getUsers() }
        coVerify(exactly = 1) { userDao.clearUsers() }
        coVerify(exactly = 1) { userDao.insertUsers(usersResponse.toDomain().toEntity()) }
    }

    @Test
    fun `getUsers should throw exception when API call fails`() = runTest {
        // Given
        coEvery { userDao.getUsers() } returns emptyList()
        coEvery { picPayService.getUsers() } throws IOException("Network error")

        // When
        val result = try {
            usersRepository.getUsers()
        } catch (error: Exception) {
            error
        }

        // Then
        assert(result is IOException)
        assertEquals("Network error", (result as IOException).message)
        coVerify(exactly = 1) { userDao.getUsers() }
        coVerify(exactly = 1) { picPayService.getUsers() }
    }

    @Test
    fun `refreshUsers should fetch from API and update local database`() = runTest {
        // Given
        val usersResponse = listOf(UserResponse("image_url", "Name1", 0, "User1"))
        coEvery { picPayService.getUsers() } returns usersResponse

        // When
        val result = usersRepository.refreshUsers()

        // Then
        assertEquals(usersResponse.toDomain(), result)
        coVerify(exactly = 1) { userDao.clearUsers() }
        coVerify(exactly = 1) { userDao.insertUsers(usersResponse.toDomain().toEntity()) }
    }

    @Test
    fun `refreshUsers should throw exception when API fails`() = runTest {
        // Given
        coEvery { picPayService.getUsers() } throws IOException("Network error")

        // When
        val result = try {
            usersRepository.refreshUsers()
        } catch (error: Exception) {
            error
        }

        // Then
        assert(result is IOException)
        assertEquals("Network error", (result as IOException).message)
        coVerify(exactly = 0) { userDao.clearUsers() }
        coVerify(exactly = 0) { userDao.insertUsers(any()) }
    }
}
