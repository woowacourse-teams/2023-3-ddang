package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse

interface AuctionRepository {
    fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>>

    suspend fun getAuctionPreviews()
    suspend fun getAuctionDetail(id: Long): AuctionDetailResponse
    suspend fun registerAuction(auction: RegisterAuctionRequest): RegisterAuctionResponse
}
