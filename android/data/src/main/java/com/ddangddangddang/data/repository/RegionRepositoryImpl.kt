package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.RegionRemoteDataSource
import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService

class RegionRepositoryImpl private constructor(
    private val remoteDataSource: RegionRemoteDataSource,
) : RegionRepository {

    override suspend fun getFirstRegions(): ApiResponse<List<RegionDetailResponse>> {
        return remoteDataSource.getFirstRegions()
    }

    override suspend fun getSecondRegions(firstId: Long): ApiResponse<List<RegionDetailResponse>> {
        return remoteDataSource.getSecondRegions(firstId)
    }

    override suspend fun getThirdRegions(
        firstId: Long,
        secondId: Long,
    ): ApiResponse<List<RegionDetailResponse>> {
        return remoteDataSource.getThirdRegions(firstId, secondId)
    }

    companion object {
        @Volatile
        private var instance: RegionRepositoryImpl? = null

        fun getInstance(service: AuctionService): RegionRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: AuctionService): RegionRepositoryImpl {
            return RegionRepositoryImpl(RegionRemoteDataSource(service)).also { instance = it }
        }
    }
}
