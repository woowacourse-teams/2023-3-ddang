package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.request.RegisterAnswerRequest
import com.ddangddangddang.data.model.request.RegisterQuestionRequest
import com.ddangddangddang.data.model.request.ReportAnswerRequest
import com.ddangddangddang.data.model.request.ReportAuctionArticleRequest
import com.ddangddangddang.data.model.request.ReportMessageRoomRequest
import com.ddangddangddang.data.model.request.ReportQuestionRequest
import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.BidHistoryResponse
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.model.response.QnaResponse
import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.model.response.UserReviewResponse
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
        @Part image: MultipartBody.Part?,
        @Part("request") body: RequestBody,
    ): ApiResponse<ProfileResponse>

    @GET("/users/auctions/bids")
    suspend fun getMyParticipateAuctionPreviews(
        @Query("page") page: Int,
        @Query("size") size: Int?,
    ): ApiResponse<AuctionPreviewsResponse>

    @GET("/users/auctions/mine")
    suspend fun fetchMyAuctionPreviews(
        @Query("page") page: Int,
        @Query("size") size: Int?,
    ): ApiResponse<AuctionPreviewsResponse>

    @POST("reports/auctions")
    suspend fun reportAuction(@Body reportRequest: ReportAuctionArticleRequest): ApiResponse<Unit>

    @POST("reports/chat-rooms")
    suspend fun reportMessageRoom(@Body reportRequest: ReportMessageRoomRequest): ApiResponse<Unit>

    @DELETE("/auctions/{id}")
    suspend fun deleteAuction(@Path("id") auctionId: Long): ApiResponse<Unit>

    @PATCH("/device-token")
    suspend fun updateDeviceToken(@Body deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit>

    @POST("/reviews")
    suspend fun reviewUser(@Body reviewRequest: ReviewRequest): ApiResponse<Unit>

    @GET("/auctions/{auctionId}/reviews")
    suspend fun getReviewUser(@Path("auctionId") auctionId: Long): ApiResponse<UserReviewResponse>

    @GET("/auctions/{auctionId}/questions")
    suspend fun getQnas(@Path("auctionId") auctionId: Long): ApiResponse<QnaResponse>

    @POST("/questions")
    suspend fun registerQuestion(@Body registerQuestionRequest: RegisterQuestionRequest): ApiResponse<Unit>

    @POST("/questions/{questionId}/answers")
    suspend fun registerAnswer(
        @Path("questionId") questionId: Long,
        @Body registerAnswerRequest: RegisterAnswerRequest,
    ): ApiResponse<Unit>

    @DELETE("/questions/{questionId}")
    suspend fun deleteQuestion(@Path("questionId") questionId: Long): ApiResponse<Unit>

    @DELETE("/questions/answers/{answerId}")
    suspend fun deleteAnswer(@Path("answerId") answerId: Long): ApiResponse<Unit>

    @POST("/reports/questions")
    suspend fun reportQuestion(@Body reportQuestionRequest: ReportQuestionRequest): ApiResponse<Unit>

    @POST("/reports/answer")
    suspend fun reportAnswer(@Body reportAnswerRequest: ReportAnswerRequest): ApiResponse<Unit>

    @GET("/bids/{auctionId}")
    suspend fun getBidHistories(@Path("auctionId") auctionId: Long): ApiResponse<List<BidHistoryResponse>>
}
