package com.ddangddangddang.android.di

import android.app.Application
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScarletLifecycleModule {
    @ApplicationForegroundLifecycle
    @Singleton
    @Provides
    fun provideApplicationForegroundLifecycle(application: Application): Lifecycle =
        AndroidLifecycle.ofApplicationForeground(application)
}
