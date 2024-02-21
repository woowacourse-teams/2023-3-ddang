package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.interceptor.AutoRefreshInterceptor
import com.ddangddangddang.data.remote.interceptor.LoggingInterceptor
import com.ddangddangddang.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpInterceptorModule {

    @HttpLoggingInterceptorQualifier
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): Interceptor =
        LoggingInterceptor.createInstance()

    @AutoRefreshInterceptorQualifier
    @Singleton
    @Provides
    fun provideAutoRefreshInterceptor(authRepository: AuthRepository): Interceptor =
        AutoRefreshInterceptor.createInstance(authRepository)
}
