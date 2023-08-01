package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("/auctions")
    suspend fun registerAuction(@Body body: RegisterAuctionRequest): ApiResponse<RegisterAuctionResponse>

    @GET("/categories")
    suspend fun fetchMainCategories(): ApiResponse<List<EachCategoryResponse>>

    @GET("/categories/{id}")
    suspend fun fetchSubCategories(@Path("id") mainId: Long): ApiResponse<List<EachCategoryResponse>>
}
