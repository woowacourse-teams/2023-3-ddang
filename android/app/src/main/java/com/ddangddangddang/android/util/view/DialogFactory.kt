package com.ddangddangddang.android.util.view

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ddangddangddang.android.R

fun Activity.showDialog(
    @StringRes
    titleId: Int? = null,
    @StringRes
    messageId: Int? = null,
    @StringRes
    negativeStringId: Int? = null,
    @StringRes
    positiveStringId: Int = R.string.all_dialog_default_positive_button,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
    isCancelable: Boolean = true,
) {
    AlertDialog.Builder(this).apply {
        titleId?.let { setTitle(getString(it)) }
        messageId?.let { setMessage(getString(messageId)) }
        negativeStringId?.let {
            setNegativeButton(negativeStringId) { _, _ ->
                actionNegative()
            }
        }
        setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        setCancelable(isCancelable)
    }.show()
}

fun Fragment.showDialog(
    @StringRes
    titleId: Int? = null,
    @StringRes
    messageId: Int? = null,
    @StringRes
    negativeStringId: Int? = null,
    @StringRes
    positiveStringId: Int = R.string.all_dialog_default_positive_button,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
    isCancelable: Boolean = true,
) {
    AlertDialog.Builder(requireContext()).apply {
        titleId?.let { setTitle(getString(titleId)) }
        messageId?.let { setMessage(getString(messageId)) }
        negativeStringId?.let {
            setNegativeButton(negativeStringId) { _, _ ->
                actionNegative()
            }
        }
        setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        setCancelable(isCancelable)
    }.show()
}
