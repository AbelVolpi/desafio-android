package com.picpay.desafio.android.data.di

import com.picpay.desafio.android.data.repository.UsersRepositoryImpl
import com.picpay.desafio.android.domain.repository.UsersRepository
import org.koin.dsl.module

val dataModule = module {
    single<UsersRepository> { UsersRepositoryImpl(get(), get()) }
}
