package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.SortType
import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.request.ReportAuctionArticleRequest
import com.ddangddangddang.data.model.request.ReportMessageRoomRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.QnaResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AuctionRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun getAuctionPreviews(
        page: Int,
        size: Int?,
        sortType: SortType?,
        title: String?,
    ): ApiResponse<AuctionPreviewsResponse> =
        service.fetchAuctionPreviews(page, size, sortType?.nameBy, title)

    suspend fun getAuctionPreviewsByTitle(
        page: Int,
        size: Int?,
        title: String,
    ): ApiResponse<AuctionPreviewsResponse> =
        service.fetchAuctionPreviews(page = page, size = size, sortType = null, title = title)

    suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> =
        service.fetchAuctionDetail(id)

    suspend fun registerAuction(
        images: List<File>,
        auction: RegisterAuctionRequest,
    ): ApiResponse<AuctionPreviewResponse> {
        val files = images.map {
            val fileBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", it.name, fileBody)
        }
        val body = Json.encodeToString(RegisterAuctionRequest.serializer(), auction)
            .toRequestBody("application/json".toMediaType())
        return service.registerAuction(files, body)
    }

    suspend fun submitAuctionBid(
        auctionBidRequest: AuctionBidRequest,
    ): ApiResponse<Unit> = service.submitAuctionBid(auctionBidRequest)

    suspend fun reportAuction(reportRequest: ReportAuctionArticleRequest): ApiResponse<Unit> =
        service.reportAuction(reportRequest)

    suspend fun reportMessageRoom(reportRequest: ReportMessageRoomRequest): ApiResponse<Unit> =
        service.reportMessageRoom(reportRequest)

    suspend fun deleteAuction(id: Long): ApiResponse<Unit> = service.deleteAuction(id)

    suspend fun getAuctionQnas(auctionId: Long): ApiResponse<QnaResponse> = service.getQnas(auctionId)
}
