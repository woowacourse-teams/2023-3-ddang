package com.ddangddangddang.android.util.view

import android.app.Activity
import com.ddangddangddang.android.R

class BackKeyHandler(val activity: Activity) {
    private var backPressedTime = 0L
    fun onBackPressed() {
        if ((System.currentTimeMillis() - backPressedTime) > 2000L) {
            backPressedTime = System.currentTimeMillis()
            Toaster.showShort(activity, activity.getString(R.string.all_back_key_check_message))
        } else {
            activity.finish()
        }
    }
}
