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

@Module
@InstallIn(SingletonComponent::class)
object FormatterModule {
    @DateFormatter
    @Provides
    fun provideDateFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.message_room_item_date_format),
            Locale.KOREAN,
        )
    }

    @TimeFormatter
    @Provides
    fun provideTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.message_room_item_time_format),
            Locale.KOREAN,
        )
    }
}
