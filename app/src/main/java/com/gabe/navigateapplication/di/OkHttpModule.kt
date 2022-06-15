package com.gabe.navigateapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

//@InstallIn(ApplicationComponent::class)
@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
    @Singleton
    @Provides
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }
}
