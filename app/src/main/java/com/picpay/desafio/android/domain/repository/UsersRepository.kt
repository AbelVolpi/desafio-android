package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User

interface UsersRepository {
    suspend fun getUsers(): List<User>
}