package com.ddangddangddang.android.util.view

import android.view.View
import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    @StringRes
    textId: Int,
    @StringRes
    actionId: Int = R.string.all_snackbar_default_action,
    action: () -> Unit = {},
) {
    Snackbar.make(this, context.getString(textId), Snackbar.LENGTH_SHORT)
        .setAction(actionId) {
            action()
        }
        .show()
}
