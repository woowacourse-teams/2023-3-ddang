package com.ddangddangddang.android.util.view

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ddangddangddang.android.R

fun Activity.showDialog(
    @StringRes
    titleId: Int = R.string.all_dialog_default_title,
    @StringRes
    messageId: Int,
    @StringRes
    negativeStringId: Int = R.string.all_dialog_default_negative_button,
    @StringRes
    positiveStringId: Int = R.string.all_dialog_default_positive_button,
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
