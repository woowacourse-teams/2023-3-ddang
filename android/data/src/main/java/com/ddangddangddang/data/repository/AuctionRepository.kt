package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.model.SortType
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.ApiResponse
import java.io.File

interface AuctionRepository {
    fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>>

    suspend fun getAuctionPreviews(
        page: Int,
        size: Int? = null,
        sortType: SortType? = null,
        title: String? = null,
    ): ApiResponse<AuctionPreviewsResponse>

    suspend fun getAuctionPreviews(
        lastAuctionId: Long?,
        size: Int,
        title: String,
    ): ApiResponse<AuctionPreviewsResponse>

    suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse>

    suspend fun registerAuction(
        images: List<File>,
        auction: RegisterAuctionRequest,
    ): ApiResponse<AuctionPreviewResponse>

    suspend fun submitAuctionBid(
        auctionId: Long,
        bidPrice: Int,
    ): ApiResponse<Unit>

    suspend fun reportAuction(auctionId: Long, description: String): ApiResponse<Unit>
    suspend fun deleteAuction(auctionId: Long): ApiResponse<Unit>
}
