package com.example.ezetap_assignment.di

import android.content.Context
import com.example.network.NetworkCalls

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesNetworkCall(@ApplicationContext app: Context): NetworkCalls =
        NetworkCalls(app)

}