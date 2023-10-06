package com.ddangddangddang.data.remote

import java.io.IOException

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val body: T) : ApiResponse<T>()
    data class Failure(val responseCode: Int, val error: String?) : ApiResponse<Nothing>()
    data class NetworkError(val exception: IOException) : ApiResponse<Nothing>()
    data class Unexpected(val t: Throwable?) : ApiResponse<Nothing>()
}
