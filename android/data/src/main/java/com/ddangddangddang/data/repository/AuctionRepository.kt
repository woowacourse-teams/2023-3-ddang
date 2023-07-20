package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse

interface AuctionRepository {
    suspend fun getAuctionPreviews(): AuctionPreviewsResponse
    suspend fun getAuctionDetail(id: Long): AuctionDetailResponse
    suspend fun registerAuction(auction: RegisterAuctionRequest): RegisterAuctionResponse
}
