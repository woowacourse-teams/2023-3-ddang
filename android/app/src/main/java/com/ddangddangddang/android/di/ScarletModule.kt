package com.ddangddangddang.android.di

import android.app.Application
import com.ddangddangddang.android.global.MessageRoomActivityResumedLifecycle
import com.ddangddangddang.data.remote.scarlet.ScarletFactory
import com.ddangddangddang.data.remote.scarlet.WebSocketService
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScarletModule {
    @ActivityForegroundLifecycle
    @Singleton
    @Provides
    fun provideApplicationForegroundLifecycle(
        application: Application,
    ): Lifecycle = MessageRoomActivityResumedLifecycle(application, LifecycleRegistry(1000L))

    @Singleton
    @Provides
    fun provideScarlet(
        @WSChattingServerURLQualifier serverURL: String,
        @AutoRefreshHttpClientQualifier httpClient: OkHttpClient,
        @ActivityForegroundLifecycle lifecycle: Lifecycle,
    ): Scarlet = ScarletFactory.createInstance(serverURL, httpClient, lifecycle)

    @Singleton
    @Provides
    fun provideWSService(scarlet: Scarlet): WebSocketService = scarlet.create()
}
