package com.ddangddangddang.android.util.compat

import android.content.Intent
import android.os.Build
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(
    key: String,
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        getSerializableExtra(key) as? T
    }
}
