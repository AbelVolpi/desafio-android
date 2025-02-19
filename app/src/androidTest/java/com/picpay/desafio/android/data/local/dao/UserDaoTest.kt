package com.picpay.desafio.android.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.data.local.database.AppDatabase
import com.picpay.desafio.android.data.local.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUsersShouldSaveUsersInDatabase() = runTest {
        // Given
        val users = listOf(UserEntity(1, "Name1", "User1", "image_url"))
        userDao.insertUsers(users)

        // When
        val result = userDao.getUsers()

        // Then
        assertEquals(users, result)
    }

    @Test
    fun clearUsersShouldRemoveAllUsersFromDatabase() = runTest {
        // Given
        val users = listOf(UserEntity(1, "Name1", "User1", "image_url"))
        userDao.insertUsers(users)

        // When
        userDao.clearUsers()
        val result = userDao.getUsers()

        // Then
        assert(result.isEmpty())
    }
}
