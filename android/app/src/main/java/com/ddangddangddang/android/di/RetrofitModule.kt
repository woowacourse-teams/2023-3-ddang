package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.AuctionRetrofit
import com.ddangddangddang.data.remote.AuthRetrofit
import com.ddangddangddang.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @AuthRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuthRetrofit(): Retrofit = AuthRetrofit.createInstance()

    @AuctionRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuctionRetrofit(authRepository: AuthRepository): Retrofit =
        AuctionRetrofit.createInstance(authRepository)
}
