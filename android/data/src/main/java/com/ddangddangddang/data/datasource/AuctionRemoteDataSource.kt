package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AuctionRemoteDataSource(private val service: Service) {
    suspend fun getAuctionPreviews(
        lastAuctionId: Long?,
        size: Int,
    ): ApiResponse<AuctionPreviewsResponse> =
        service.fetchAuctionPreviews(lastAuctionId, size)

    suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> =
        service.fetchAuctionDetail(id)

    suspend fun registerAuction(
        images: List<File>,
        auction: RegisterAuctionRequest,
    ): ApiResponse<RegisterAuctionResponse> {
        val files = images.map {
            val fileBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, fileBody)
        }
        return service.registerAuction(auction)
    }
}
