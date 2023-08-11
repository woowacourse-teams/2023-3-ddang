package com.ddangddangddang.data.remote

import okhttp3.Request
import okio.IOException
import okio.Timeout
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
                                    IllegalStateException("Response body가 존재하지 않습니다."),
                                ),
                            ),
                        )
                    }
                } else {
                    callback.onResponse(
                        this@AuctionCall,
                        Response.success(
                            ApiResponse.Failure(
                                response.code(),
                                response.errorBody()?.string(),
                            ),
                        ),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val response = when (t) {
                    is IOException -> ApiResponse.NetworkError(t)
                    else -> ApiResponse.Unexpected(t)
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
}
