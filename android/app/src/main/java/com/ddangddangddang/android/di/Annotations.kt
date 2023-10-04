package com.ddangddangddang.android.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuctionRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DateFormatter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeFormatter
