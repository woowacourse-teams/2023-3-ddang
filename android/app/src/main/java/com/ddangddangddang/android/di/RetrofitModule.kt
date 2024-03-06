package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.retrofit.AuctionService
import com.ddangddangddang.data.remote.retrofit.AuthService
import com.ddangddangddang.data.remote.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @AuthRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuthRetrofit(
        @HttpServerURLQualifier serverURL: String,
        @DefaultHttpClientQualifier httpClient: OkHttpClient,
    ): Retrofit =
        RetrofitFactory.createInstance(serverURL, httpClient)

    @AuctionRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuctionRetrofit(
        @HttpServerURLQualifier serverURL: String,
        @AutoRefreshHttpClientQualifier httpClient: OkHttpClient,
    ): Retrofit =
        RetrofitFactory.createInstance(serverURL, httpClient)

    @Singleton
    @Provides
    fun provideAuthService(@AuthRetrofitQualifier retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideAuctionService(@AuctionRetrofitQualifier retrofit: Retrofit): AuctionService =
        retrofit.create(AuctionService::class.java)
}
