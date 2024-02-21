package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.client.AutoRefreshHttpClient
import com.ddangddangddang.data.remote.client.DefaultHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @DefaultHttpClientQualifier
    @Singleton
    @Provides
    fun provideDefaultHttpClient(@HttpLoggingInterceptorQualifier loggingInterceptor: Interceptor): OkHttpClient =
        DefaultHttpClient.createOkHttpClient(loggingInterceptor)

    @AutoRefreshHttpClientQualifier
    @Singleton
    @Provides
    fun provideAutoRefreshHttpClient(
        @HttpLoggingInterceptorQualifier loggingInterceptor: Interceptor,
        @AutoRefreshInterceptorQualifier autoRefreshInterceptor: Interceptor,
    ): OkHttpClient =
        AutoRefreshHttpClient.createInstance(loggingInterceptor, autoRefreshInterceptor)
}
