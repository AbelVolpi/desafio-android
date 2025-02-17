package com.picpay.desafio.android.presentation.di

import com.picpay.desafio.android.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get()) }
}