package com.example.mealplan.utils.di

import com.example.mealplan.BuildConfig
import com.example.mealplan.data.network.ApiServices
import com.example.mealplan.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideConnectionTimeout() = Constants.CONNECTION_TIME_OUT

    @Provides
    @Singleton
    @Named(Constants.PING_NAMED)
    fun providePingTimeout() = Constants.PING_TIME_OUT

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    }

    @Provides
    @Singleton
    fun provideClient(
        interceptor: HttpLoggingInterceptor,
        timeout: Long,
        @Named(Constants.PING_NAMED) ping: Long
    ) =
        OkHttpClient.Builder().addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                addHeader(Constants.AUTH, Constants.API_KEY)
            }.build())
        }.addInterceptor(interceptor).connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).pingInterval(ping, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, baseUrl: String, gson: Gson): ApiServices =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiServices::class.java)
}