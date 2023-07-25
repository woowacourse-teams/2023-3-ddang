package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.Service
import java.lang.IllegalArgumentException

class AuctionRemoteDataSource(private val service: Service) {
    suspend fun getAuctionPreviews(): AuctionPreviewsResponse {
        val response = service.fetchAuctionPreviews()
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
    }

    suspend fun getAuctionDetail(id: Long): AuctionDetailResponse {
        val response = service.fetchAuctionDetail(id)
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
    }

    suspend fun registerAuction(auction: RegisterAuctionRequest): RegisterAuctionResponse {
        val response = service.registerAuction(auction)
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
    }
}
