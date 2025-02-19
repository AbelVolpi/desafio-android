package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.entity.toDomain
import com.picpay.desafio.android.data.local.entity.toEntity
import com.picpay.desafio.android.data.remote.model.toDomain
import com.picpay.desafio.android.data.remote.service.PicPayService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val picPayService: PicPayService,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {

    override suspend fun getUsers(): List<User> {
        return withContext(dispatcher) {
            val cachedUsers = userDao.getUsers().toDomain()
            if (cachedUsers.isNotEmpty()) return@withContext cachedUsers

            fetchAndCacheUsers()
        }
    }

    override suspend fun refreshUsers(): List<User> {
        return withContext(dispatcher) {
            fetchAndCacheUsers()
        }
    }

    private suspend fun fetchAndCacheUsers(): List<User> {
        return try {
            val usersResponse = picPayService.getUsers()
            val users = usersResponse.toDomain()

            userDao.clearUsers()
            userDao.insertUsers(users.toEntity())

            users
        } catch (e: Exception) {
            throw e
        }
    }
}
