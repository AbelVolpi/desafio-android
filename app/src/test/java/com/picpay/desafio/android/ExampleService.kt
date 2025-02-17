package com.picpay.desafio.android

import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.data.service.PicPayService

class ExampleService(
    private val service: PicPayService
) {

   suspend fun example(): List<UserResponse> {
        return emptyList()
    }
}