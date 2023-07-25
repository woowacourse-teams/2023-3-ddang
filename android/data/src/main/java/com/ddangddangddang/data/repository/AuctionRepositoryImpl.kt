package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.remote.Service
import java.lang.IllegalArgumentException

class AuctionRepositoryImpl private constructor(private val service: Service) : AuctionRepository {
    override suspend fun getAuctionPreviews(): AuctionPreviewsResponse {
        val response = service.fetchAuctionPreviews()
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
    }

    override suspend fun getAuctionDetail(id: Long): AuctionDetailResponse {
        val response = service.fetchAuctionDetail(id)
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
    }

    override suspend fun registerAuction(auction: RegisterAuctionRequest): RegisterAuctionResponse {
        val response = service.registerAuction(auction)
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw IllegalArgumentException()
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
            return AuctionRepositoryImpl(service)
        }
    }
}
