package com.ddangddangddang.android.feature.common

import androidx.annotation.StringRes
import com.ddangddangddang.android.R

sealed class ErrorType {
    data class FAILURE(val message: String?) : ErrorType()
    object NETWORK_ERROR : ErrorType() {
        @StringRes val messageId: Int = R.string.all_network_error_message
    }
    object UNEXPECTED : ErrorType() {
        @StringRes val messageId: Int = R.string.all_unexpected_error_message
    }
}
