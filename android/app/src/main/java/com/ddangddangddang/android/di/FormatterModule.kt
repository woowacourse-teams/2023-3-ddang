package com.ddangddangddang.android.di

import android.content.Context
import com.ddangddangddang.android.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FormatterModule {
    @DateFormatter
    @Singleton
    @Provides
    fun provideDateFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.all_date_format),
            Locale.KOREAN,
        )
    }

    @TimeFormatter
    @Singleton
    @Provides
    fun provideTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.all_time_format),
            Locale.KOREAN,
        )
    }

    @DefaultDateTimeFormatter
    @Singleton
    @Provides
    fun provideDefaultDateTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.all_date_time_format),
            Locale.KOREAN,
        )
    }
}
