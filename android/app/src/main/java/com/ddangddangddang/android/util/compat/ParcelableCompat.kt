package com.ddangddangddang.android.util.compat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

inline fun <reified T : Parcelable> Intent.getParcelableCompat(key: String): T? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val bundle = extras ?: return null
        return BundleCompat.getParcelable(bundle, key, T::class.java)
    }
    @Suppress("DEPRECATION")
    return getParcelableExtra(key) as? T
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key) as? T
    }
}
