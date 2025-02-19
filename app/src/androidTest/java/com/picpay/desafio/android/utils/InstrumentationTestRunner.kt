package com.picpay.desafio.android.utils

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.picpay.desafio.android.data.di.dataModule
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.database.AppDatabase
import com.picpay.desafio.android.data.remote.service.PicPayService
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
            modules(
                instrumentedTestNetworkModule,
                instrumentedDatabaseModule,
                dataModule,
                presentationModule
            )
        }
    }
}

val instrumentedDatabaseModule = module {
    single {
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().getTargetContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }
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
