package com.ddangddangddang.android.feature.common

import android.text.Editable
import android.text.TextWatcher

class PriceTextWatcher(private val onAfterChanged: (String) -> Unit) : TextWatcher {
    private var cursorPositionFromEnd: Int = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let { str ->
            cursorPositionFromEnd = str.length - (start + count)
            moveCursorIfOnlyCommaRemoved(str, before)
        }
    }

    private fun moveCursorIfOnlyCommaRemoved(str: CharSequence, before: Int) {
        val strOnlyNumber = str.filter { it.isDigit() }
        val expectedCommaCount = (strOnlyNumber.length - 1) / 3

        // 쉼표의 개수가 적고 지워진 문자가 1개인 경우 커서를 앞으로 1 움직입니다.
        val actualCommaCount = str.count { it == ',' }
        if (actualCommaCount < expectedCommaCount && before == 1) {
            cursorPositionFromEnd++
        }
    }

    override fun afterTextChanged(s: Editable?) {
        s?.let { onAfterChanged(s.toString()) }
    }

    fun getCursorPosition(
        textLength: Int,
        defaultCursorPositionFromEnd: Int,
    ): Int {
        cursorPositionFromEnd =
            if (cursorPositionFromEnd > 0) cursorPositionFromEnd else defaultCursorPositionFromEnd
        val cursorPosition = textLength - cursorPositionFromEnd
        return if (cursorPosition > 0) cursorPosition else 0
    }
}
