package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.request.ReportRequest
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.model.response.RegionDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuctionService {
    @GET("/auctions")
    suspend fun fetchAuctionPreviews(
        @Query("page") page: Int,
        @Query("size") size: Int?,
        @Query("sortType") sortType: String?,
        @Query("title") title: String?,
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
        @Body auctionBidRequest: AuctionBidRequest,
    ): ApiResponse<Unit>

    @GET("/regions")
    suspend fun fetchFirstRegions(): ApiResponse<List<RegionDetailResponse>>

    @GET("/regions/{firstId}")
    suspend fun fetchSecondRegions(@Path("firstId") firstId: Long): ApiResponse<List<RegionDetailResponse>>

    @GET("/regions/{firstId}/{secondId}")
    suspend fun fetchThirdRegions(
        @Path("firstId") firstId: Long,
        @Path("secondId") secondId: Long,
    ): ApiResponse<List<RegionDetailResponse>>

    @GET("/categories")
    suspend fun fetchMainCategories(): ApiResponse<List<EachCategoryResponse>>

    @GET("/categories/{id}")
    suspend fun fetchSubCategories(@Path("id") mainId: Long): ApiResponse<List<EachCategoryResponse>>

    @POST("/chattings")
    suspend fun getChatRoomId(
        @Body createChatRoomRequest: GetChatRoomIdRequest,
    ): ApiResponse<ChatRoomIdResponse>

    @GET("/chattings")
    suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>>

    @GET("/chattings/{chatRoomId}")
    suspend fun getChatRoomPreview(@Path("chatRoomId") chatRoomId: Long): ApiResponse<ChatRoomPreviewResponse>

    @GET("/chattings/{chatRoomId}/messages")
    suspend fun getMessages(
        @Path("chatRoomId") chatRoomId: Long,
        @Query("lastMessageId") lastMessageId: Long?,
    ): ApiResponse<List<ChatMessageResponse>>

    @POST("/chattings/{chatRoomId}/messages")
    suspend fun sendMessage(
        @Path("chatRoomId") chatRoomId: Long,
        @Body chatMessageRequest: ChatMessageRequest,
    ): ApiResponse<ChatMessageIdResponse>

    @GET("/users")
    suspend fun fetchProfile(): ApiResponse<ProfileResponse>

    @Multipart
    @PATCH("/users")
    suspend fun updateProfile(
        @Part image: MultipartBody.Part,
        @Part("request") body: RequestBody,
    ): ApiResponse<ProfileResponse>

    @GET("/users/auctions/mine")
    suspend fun fetchMyAuctionPreviews(
        @Query("page") page: Int,
    ): ApiResponse<AuctionPreviewsResponse>

    @POST("reports/auctions")
    suspend fun reportAuction(@Body reportRequest: ReportRequest): ApiResponse<Unit>

    @DELETE("/auctions/{id}")
    suspend fun deleteAuction(@Path("id") auctionId: Long): ApiResponse<Unit>

    @PATCH("/device-token")
    suspend fun updateDeviceToken(@Body deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit>
}
