package com.ddangddangddang.android.util.view

import android.content.res.Resources

fun convertDpToPx(dp: Float): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (dp * density + 0.5f).toInt()
}
