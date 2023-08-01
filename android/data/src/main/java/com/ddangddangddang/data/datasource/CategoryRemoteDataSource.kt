package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class CategoryRemoteDataSource(private val service: Service) {
    suspend fun getMainCategories(): ApiResponse<List<EachCategoryResponse>> =
        service.fetchMainCategories()

    suspend fun getSubCategories(id: Long): ApiResponse<List<EachCategoryResponse>> =
        service.fetchSubCategories(id)
}
