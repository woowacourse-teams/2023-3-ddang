package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class AuctionRemoteDataSource(private val service: Service) {
    suspend fun getAuctionPreviews(lastAuctionId: Long?, size: Int): ApiResponse<AuctionPreviewsResponse> =
        service.fetchAuctionPreviews(lastAuctionId, size)

    suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> =
        service.fetchAuctionDetail(id)

    suspend fun registerAuction(auction: RegisterAuctionRequest): ApiResponse<RegisterAuctionResponse> =
        service.registerAuction(auction)
}
