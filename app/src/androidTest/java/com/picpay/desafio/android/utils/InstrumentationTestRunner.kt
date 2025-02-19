package com.picpay.desafio.android.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.picpay.desafio.android.data.di.dataModule
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.presentation.di.presentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestApplication::class.java.name, context)
    }
}

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(instrumentedTestNetworkModule, dataModule, presentationModule)
        }
    }
}

val instrumentedTestNetworkModule = module {
    single { provideTestRetrofit() }
    single { provideTestPicPayService(get()) }
}

private fun provideTestRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

private fun provideTestPicPayService(retrofit: Retrofit): PicPayService {
    return retrofit.create(PicPayService::class.java)
}



