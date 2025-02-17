package com.picpay.desafio.android.data.di

import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.data.service.PicPayService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideRetrofit(get(), get()) }
    single { provideOkHttpClient() }
    single { provideConverterFactory() }
    single { providePicPayService(get()) }
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val timeout = 3L
    return OkHttpClient.Builder()
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
}

private fun provideConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

private fun providePicPayService(retrofit: Retrofit): PicPayService {
    return retrofit.create(PicPayService::class.java)
}


