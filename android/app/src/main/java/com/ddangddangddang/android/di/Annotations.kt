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
annotation class HttpServerURLQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WSChattingServerURLQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActivityForegroundLifecycle

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultHttpClientQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AutoRefreshHttpClientQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpLoggingInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AutoRefreshInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DateFormatter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeFormatter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDateTimeFormatter
