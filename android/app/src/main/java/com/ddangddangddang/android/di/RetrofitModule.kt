package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.retrofit.AuctionRetrofit
import com.ddangddangddang.data.remote.retrofit.AuthRetrofit
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
    fun provideAuthRetrofit(@DefaultHttpClientQualifier httpClient: OkHttpClient): Retrofit =
        AuthRetrofit.createInstance(httpClient)

    @AuctionRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuctionRetrofit(@AutoRefreshHttpClientQualifier httpClient: OkHttpClient): Retrofit =
        AuctionRetrofit.createInstance(httpClient)
}
