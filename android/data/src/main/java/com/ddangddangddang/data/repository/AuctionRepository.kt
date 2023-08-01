package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.ApiResponse
import java.io.File

interface AuctionRepository {
    fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>>

    suspend fun getAuctionPreviews(lastAuctionId: Long?, size: Int): ApiResponse<AuctionPreviewsResponse>
    suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse>
    suspend fun registerAuction(images: List<File>, auction: RegisterAuctionRequest): ApiResponse<RegisterAuctionResponse>
}
