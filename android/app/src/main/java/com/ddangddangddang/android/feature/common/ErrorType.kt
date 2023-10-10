package com.ddangddangddang.android.feature.common

import com.ddangddangddang.android.R
import com.ddangddangddang.android.global.DdangDdangDdang

sealed class ErrorType(open val message: String?) {
    data class FAILURE(override val message: String?) : ErrorType(message)
    object NETWORK_ERROR :
        ErrorType(DdangDdangDdang.resources.getString(R.string.all_network_error_message))
    object UNEXPECTED :
        ErrorType(DdangDdangDdang.resources.getString(R.string.all_unexpected_error_message))
}
