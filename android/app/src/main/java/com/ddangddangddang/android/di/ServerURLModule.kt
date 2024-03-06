package com.ddangddangddang.android.di

import com.ddangddangddang.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerURLModule {
    @HttpServerURLQualifier
    @Singleton
    @Provides
    fun provideHttpServerURL(): String {
        return if (BuildConfig.DEBUG) BuildConfig.HTTP_TEST_URL else BuildConfig.HTTP_BASE_URL
    }

    @WSChattingServerURLQualifier
    @Singleton
    @Provides
    fun provideWSChattingServerURL(): String {
        return BuildConfig.WS_CHATTINGS_BASE_URL
    }
}
