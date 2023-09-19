package com.ddangddangddang.android.feature.register

import android.text.Editable
import android.text.TextWatcher

class PriceTextWatcher(private val onChanged: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        s?.let { onChanged(s.toString()) }
    }
}
