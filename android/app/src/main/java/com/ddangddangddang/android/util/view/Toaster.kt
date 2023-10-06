package com.ddangddangddang.android.util.view

import android.content.Context
import android.widget.Toast

object Toaster {
    private var toast: Toast? = null

    fun showShort(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { this.show() }
    }

    fun showLong(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG).apply { this.show() }
    }

    fun cancel() {
        toast?.cancel()
    }
}
