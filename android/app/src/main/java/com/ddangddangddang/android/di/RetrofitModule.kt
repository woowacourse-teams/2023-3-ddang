package com.ddangddangddang.android.di

import com.ddangddangddang.data.BuildConfig
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
    fun provideAuthRetrofit(@ServerURLQualifier serverURL: String, @DefaultHttpClientQualifier httpClient: OkHttpClient): Retrofit =
        RetrofitFactory.createInstance(serverURL, httpClient)

    @AuctionRetrofitQualifier
    @Singleton
    @Provides
    fun provideAuctionRetrofit(@ServerURLQualifier serverURL: String, @AutoRefreshHttpClientQualifier httpClient: OkHttpClient): Retrofit =
        RetrofitFactory.createInstance(serverURL, httpClient)

    @ServerURLQualifier
    @Singleton
    @Provides
    fun provideServerURL(): String {
        return if (BuildConfig.DEBUG) BuildConfig.TEST_URL else BuildConfig.BASE_URL
    }
}
