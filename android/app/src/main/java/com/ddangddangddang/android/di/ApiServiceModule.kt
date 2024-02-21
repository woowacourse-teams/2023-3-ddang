package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.retrofit.AuctionService
import com.ddangddangddang.data.remote.retrofit.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Singleton
    @Provides
    fun provideAuthService(@AuthRetrofitQualifier retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideAuctionService(@AuctionRetrofitQualifier retrofit: Retrofit): AuctionService =
        retrofit.create(AuctionService::class.java)
}
