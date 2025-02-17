package com.picpay.desafio.android.data.utils

import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.domain.model.User

fun List<UserResponse>.toDomain(): List<User> {
    return this.map {
        User(
            img = it.img,
            name = it.name,
            id = it.id,
            username = it.username
        )
    }
}