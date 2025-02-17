package com.picpay.desafio.android

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.data.service.PicPayService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ExampleServiceTest {

    private val api = mock<PicPayService>()

    private val service = ExampleService(api)

    @Test
    fun exampleTest() = runTest {
        // given
        val expectedUserResponses = emptyList<UserResponse>()

        whenever(api.getUsers()).thenReturn(expectedUserResponses)

        // when
        val users = service.example()

        // then
        assertEquals(users, expectedUserResponses)
    }
}