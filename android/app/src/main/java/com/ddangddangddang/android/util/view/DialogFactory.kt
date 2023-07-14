package com.ddangddangddang.android.util.view

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Activity.showDialog(
    @StringRes
    titleId: Int,
    @StringRes
    messageId: Int,
    @StringRes
    negativeStringId: Int,
    @StringRes
    positiveStringId: Int,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
) {
    AlertDialog.Builder(this)
        .setTitle(getString(titleId))
        .setMessage(getString(messageId))
        .setNegativeButton(negativeStringId) { _, _ ->
            actionNegative()
        }
        .setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        .show()
}

fun Fragment.showDialog(
    @StringRes
    titleId: Int,
    @StringRes
    messageId: Int,
    @StringRes
    negativeStringId: Int,
    @StringRes
    positiveStringId: Int,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
) {
    AlertDialog.Builder(requireContext())
        .setTitle(getString(titleId))
        .setMessage(getString(messageId))
        .setNegativeButton(negativeStringId) { _, _ ->
            actionNegative()
        }
        .setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        .show()
}
