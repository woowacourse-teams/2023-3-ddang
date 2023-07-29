package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.datasource.AuctionLocalDataSource
import com.ddangddangddang.data.datasource.AuctionRemoteDataSource
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class AuctionRepositoryImpl private constructor(
    private val localDataSource: AuctionLocalDataSource,
    private val remoteDataSource: AuctionRemoteDataSource,
) : AuctionRepository {

    override fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>> {
        return localDataSource.observeAuctionPreviews()
    }

    override suspend fun getAuctionPreviews(lastAuctionId: Long?, size: Int): ApiResponse<AuctionPreviewsResponse> {
        val response = remoteDataSource.getAuctionPreviews(lastAuctionId, size)
        if (response is ApiResponse.Success) {
            localDataSource.addAuctionPreviews(response.body.auctions)
        }
        return response
    }

    override suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> {
        return remoteDataSource.getAuctionDetail(id)
    }

    override suspend fun registerAuction(auction: RegisterAuctionRequest): ApiResponse<RegisterAuctionResponse> {
        val response = remoteDataSource.registerAuction(auction)
        if (response is ApiResponse.Success) {
            val auctionPreviewResponse = AuctionPreviewResponse(
                response.body.id,
                auction.title,
                auction.images.firstOrNull() ?: "",
                auction.startPrice,
                "UNBIDDEN",
                0,
            )
            localDataSource.addAuctionPreview(auctionPreviewResponse)
        }
        return response
    }

    companion object {
        @Volatile
        private var instance: AuctionRepositoryImpl? = null

        fun getInstance(service: Service): AuctionRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: Service): AuctionRepositoryImpl {
            val localDataSource = AuctionLocalDataSource()
            val remoteDataSource = AuctionRemoteDataSource(service)
            return AuctionRepositoryImpl(localDataSource, remoteDataSource)
                .also { instance = it }
        }
    }
}
