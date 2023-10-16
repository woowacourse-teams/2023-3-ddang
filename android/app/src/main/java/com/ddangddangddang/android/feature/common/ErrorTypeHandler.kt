package com.ddangddangddang.android.feature.common

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.showSnackbar

fun Activity.notifyFailureMessage(errorType: ErrorType, @StringRes defaultMessageId: Int) {
    val defaultMessage = getString(defaultMessageId)
    Toaster.showShort(this, errorType.message ?: defaultMessage)
}

fun Activity.notifyFailureSnackBar(
    anchorView: View,
    errorType: ErrorType,
    @StringRes defaultMessageId: Int,
    @StringRes actionMessageId: Int,
    action: () -> Unit = {},
) {
    val defaultMessage = getString(defaultMessageId)
    val actionMessage = getString(actionMessageId)
    anchorView.showSnackbar(
        message = errorType.message ?: defaultMessage,
        actionMessage = actionMessage,
        action,
    )
}

fun Fragment.notifyFailureSnackBar(
    anchorView: View,
    errorType: ErrorType,
    @StringRes defaultMessageId: Int,
    @StringRes actionMessageId: Int,
    action: () -> Unit = {},
) {
    val defaultMessage = getString(defaultMessageId)
    val actionMessage = getString(actionMessageId)
    anchorView.showSnackbar(
        message = errorType.message ?: defaultMessage,
        actionMessage = actionMessage,
        action,
    )
}
