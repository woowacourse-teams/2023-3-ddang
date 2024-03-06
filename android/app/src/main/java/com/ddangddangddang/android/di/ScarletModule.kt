package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.scarlet.ChattingService
import com.ddangddangddang.data.remote.scarlet.ScarletFactory
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ActivityRetainedComponent::class)
object ScarletModule {
    @ActivityRetainedScoped
    @Provides
    fun provideScarlet(
        @WSChattingServerURLQualifier serverURL: String,
        @AutoRefreshHttpClientQualifier httpClient: OkHttpClient,
        @ApplicationForegroundLifecycle lifecycle: Lifecycle,
    ): Scarlet = ScarletFactory.createInstance(serverURL, httpClient, lifecycle)

    @ActivityRetainedScoped
    @Provides
    fun provideChattingService(scarlet: Scarlet): ChattingService = scarlet.create()
}
