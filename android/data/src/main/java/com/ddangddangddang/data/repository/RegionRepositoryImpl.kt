package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.RegionRemoteDataSource
import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import javax.inject.Inject

class RegionRepositoryImpl @Inject constructor(
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
}
