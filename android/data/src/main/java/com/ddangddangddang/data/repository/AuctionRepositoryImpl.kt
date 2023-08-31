package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.datasource.AuctionLocalDataSource
import com.ddangddangddang.data.datasource.AuctionRemoteDataSource
import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.request.ReportRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
import java.io.File

class AuctionRepositoryImpl private constructor(
    private val localDataSource: AuctionLocalDataSource,
    private val remoteDataSource: AuctionRemoteDataSource,
) : AuctionRepository {

    override fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>> {
        return localDataSource.observeAuctionPreviews()
    }

    override suspend fun getAuctionPreviews(
        lastAuctionId: Long?,
        size: Int,
    ): ApiResponse<AuctionPreviewsResponse> {
        val response = remoteDataSource.getAuctionPreviews(lastAuctionId, size)
        if (response is ApiResponse.Success) {
            if (lastAuctionId == null) localDataSource.clearAuctionPreviews()
            localDataSource.addAuctionPreviews(response.body.auctions)
        }
        return response
    }

    override suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> {
        val response = remoteDataSource.getAuctionDetail(id)
        if (response is ApiResponse.Success) {
            localDataSource.updateAuctionPreview(response.body)
        }
        if (response is ApiResponse.Failure && response.responseCode == 404) {
            localDataSource.removeAuctionPreview(id)
        }
        return response
    }

    override suspend fun registerAuction(
        images: List<File>,
        auction: RegisterAuctionRequest,
    ): ApiResponse<AuctionPreviewResponse> {
        val response = remoteDataSource.registerAuction(images, auction)
        if (response is ApiResponse.Success) {
            localDataSource.addAuctionPreview(response.body)
        }
        return response
    }

    override suspend fun submitAuctionBid(
        auctionId: Long,
        bidPrice: Int,
    ): ApiResponse<Unit> {
        return remoteDataSource.submitAuctionBid(AuctionBidRequest(auctionId, bidPrice))
    }

    override suspend fun reportAuction(auctionId: Long, description: String): ApiResponse<Unit> {
        return remoteDataSource.reportAuction(ReportRequest(auctionId, description))
    }

    override suspend fun deleteAuction(auctionId: Long): ApiResponse<Unit> {
        val response = remoteDataSource.deleteAuction(auctionId)
        if (response is ApiResponse.Success) localDataSource.removeAuctionPreview(auctionId)
        return response
    }

    companion object {
        @Volatile
        private var instance: AuctionRepositoryImpl? = null

        fun getInstance(service: AuctionService): AuctionRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: AuctionService): AuctionRepositoryImpl {
            val localDataSource = AuctionLocalDataSource()
            val remoteDataSource = AuctionRemoteDataSource(service)
            return AuctionRepositoryImpl(localDataSource, remoteDataSource)
                .also { instance = it }
        }
    }
}
