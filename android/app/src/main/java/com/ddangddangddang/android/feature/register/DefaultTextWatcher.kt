package com.ddangddangddang.android.feature.register

import android.text.Editable
import android.text.TextWatcher

class DefaultTextWatcher(private val onAfterChanged: (Int, String) -> Unit) : TextWatcher {
    private var cursorPositionFromEnd: Int = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            cursorPositionFromEnd = it.length - (start + count)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        s?.let { onAfterChanged(cursorPositionFromEnd, s.toString()) }
    }
}
