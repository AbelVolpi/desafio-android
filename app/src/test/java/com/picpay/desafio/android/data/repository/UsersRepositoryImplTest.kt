import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.data.repository.UsersRepositoryImpl
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.data.utils.toDomain
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

    private lateinit var usersRepository: UsersRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        usersRepository = UsersRepositoryImpl(picPayService, testDispatcher)
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
}

