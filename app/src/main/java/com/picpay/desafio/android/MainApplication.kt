package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.data.di.dataModule
import com.picpay.desafio.android.data.di.networkModule
import com.picpay.desafio.android.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        org.koin.core.context.startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(networkModule, dataModule, presentationModule)
        }
    }
}