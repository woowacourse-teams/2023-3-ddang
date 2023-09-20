package com.ddangddangddang.android.di

import android.content.Context
import com.ddangddangddang.data.local.AuthSharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Singleton
    @Provides
    fun provideAuthSharedPreferences(@ApplicationContext context: Context): AuthSharedPreference =
        AuthSharedPreference(context)
}
