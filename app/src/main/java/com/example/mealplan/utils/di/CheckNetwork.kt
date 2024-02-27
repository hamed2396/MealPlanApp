package com.example.mealplan.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.example.mealplan.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckNetwork {
    @Provides
    @Singleton
    fun provideCM(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideRequest(): NetworkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        //ANDROID M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        //ANDROID P
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)

        }
    }.build()

}