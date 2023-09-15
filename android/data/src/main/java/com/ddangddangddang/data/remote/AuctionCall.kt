package com.ddangddangddang.data.remote

import okhttp3.Request
import okio.IOException
import okio.Timeout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException
import java.lang.reflect.Type

class AuctionCall<T : Any>(private val call: Call<T>, private val responseType: Type) :
    Call<ApiResponse<T>> {
    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onResponse(
                            this@AuctionCall,
                            Response.success(ApiResponse.Success(it)),
                        )
                    } ?: run {
                        if (responseType == Unit::class.java) {
                            @Suppress("UNCHECKED_CAST")
                            return@run callback.onResponse(
                                this@AuctionCall,
                                Response.success(ApiResponse.Success(Unit as T)),
                            )
                        }
                        callback.onResponse(
                            this@AuctionCall,
                            Response.success(
                                ApiResponse.Unexpected(
                                    IllegalStateException(UNEXPECTED_ERROR_MESSAGE),
                                ),
                            ),
                        )
                    }
                } else {
                    val message = runCatching {
                        response.errorBody()?.let {
                            JSONObject(it.string()).getString("message")
                        }
                    }.getOrNull()
                    callback.onResponse(
                        this@AuctionCall,
                        Response.success(
                            ApiResponse.Failure(
                                response.code(),
                                message,
                            ),
                        ),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val response = when (t) {
                    is IOException -> {
                        val throwable = IOException(NETWORK_ERROR_MESSAGE, t)
                        ApiResponse.NetworkError(throwable)
                    }

                    else -> {
                        val throwable = Throwable(UNEXPECTED_ERROR_MESSAGE, t)
                        ApiResponse.Unexpected(throwable)
                    }
                }
                callback.onResponse(
                    this@AuctionCall,
                    Response.success(response),
                )
            }
        })
    }

    override fun clone(): Call<ApiResponse<T>> = AuctionCall<T>(call.clone(), responseType)

    override fun execute(): Response<ApiResponse<T>> {
        throw UnsupportedOperationException("AuctionCall은 execute를 지원하지 않습니다.")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    companion object {
        private const val NETWORK_ERROR_MESSAGE = "인터넷이 연결되어 있는지 확인해주세요."
        private const val UNEXPECTED_ERROR_MESSAGE = "예기치 못한 오류가 발생했습니다. 다시 시도해주세요."
    }
}
