package com.picpay.desafio.android.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import UiTestUtils.getJsonContent

class MockServerDispatcher{
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/users" -> MockResponse()
                    .setResponseCode(200)
                    .setBody(getJsonContent("users_success.json"))

                else -> MockResponse().setResponseCode(404)
            }
        }
    }
}

