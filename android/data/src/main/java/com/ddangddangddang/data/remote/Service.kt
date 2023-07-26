package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Service {
    @GET("/auctions")
    suspend fun fetchAuctionPreviews(): ApiResponse<AuctionPreviewsResponse>

    @GET("/auctions/{id}")
    suspend fun fetchAuctionDetail(@Path("id") id: Long): ApiResponse<AuctionDetailResponse>

    @POST("/auctions")
    suspend fun registerAuction(@Body body: RegisterAuctionRequest): ApiResponse<RegisterAuctionResponse>
}
