package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("/auctions")
    suspend fun fetchAuctionPreviews(
        @Query("lastAuctionId") id: Long?,
        @Query("size") size: Int,
    ): ApiResponse<AuctionPreviewsResponse>

    @GET("/auctions/{id}")
    suspend fun fetchAuctionDetail(@Path("id") id: Long): ApiResponse<AuctionDetailResponse>

    @Multipart
    @POST("/auctions")
    suspend fun registerAuction(
        @Part images: List<MultipartBody.Part>,
        @Part("request") body: RequestBody,
    ): ApiResponse<AuctionPreviewResponse>

    @POST("/bids")
    suspend fun submitAuctionBid(
        @Header("Authorization") authorization: String,
        @Body auctionBidRequest: AuctionBidRequest,
    ): ApiResponse<Unit>
}
