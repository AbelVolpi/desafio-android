package com.picpay.desafio.android.presentation.utils

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure<T>(val exception: Throwable) : UiState<T>()
}
