package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.remote.ApiResponse

interface CategoryRepository {
    suspend fun getMainCategories(): ApiResponse<List<EachCategoryResponse>>

    suspend fun getSubCategories(mainCategoryId: Long): ApiResponse<List<EachCategoryResponse>>
}
