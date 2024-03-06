package com.ddangddangddang.android.di

import com.ddangddangddang.data.remote.websocket.ChattingListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object WebSocketListenerModule {
    @ActivityRetainedScoped
    @Provides
    fun getChattingListener(): ChattingListener = ChattingListener()
}
