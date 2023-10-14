package com.ddangddangddang.android.feature.common

import android.app.Activity
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.ddangddangddang.android.util.view.Toaster

fun Activity.notifyFailureMessage(errorType: ErrorType, @StringRes defaultMessageId: Int) {
    val defaultMessage = getString(defaultMessageId)
    Toaster.showShort(this, errorType.message ?: defaultMessage)
}

fun Fragment.notifyFailureMessage(errorType: ErrorType, @StringRes defaultMessageId: Int) {
    val defaultMessage = getString(defaultMessageId)
    Toaster.showShort(requireContext(), errorType.message ?: defaultMessage)
}
