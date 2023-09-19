package com.ddangddangddang.android.feature.register

import android.text.Editable
import android.text.TextWatcher
import android.util.Log

class PriceTextWatcher(private val onChanged: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        Log.d("test", s.toString())
        s?.let { onChanged(s.toString()) }
    }
}
