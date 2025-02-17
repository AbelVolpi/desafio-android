package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.data.utils.toDomain
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val picPayService: PicPayService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {

    override suspend fun getUsers(): List<User> {
        return withContext(dispatcher) {
            val usersResponse = picPayService.getUsers()
           usersResponse.toDomain()
        }
    }
}