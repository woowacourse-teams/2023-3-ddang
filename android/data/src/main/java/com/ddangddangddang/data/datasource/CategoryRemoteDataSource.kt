package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.remote.retrofit.AuctionService
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun getMainCategories(): ApiResponse<List<EachCategoryResponse>> =
        service.fetchMainCategories()

    suspend fun getSubCategories(id: Long): ApiResponse<List<EachCategoryResponse>> =
        service.fetchSubCategories(id)
}
