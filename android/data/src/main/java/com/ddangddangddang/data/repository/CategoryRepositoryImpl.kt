package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.CategoryRemoteDataSource
import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.remote.ApiResponse
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource,
) : CategoryRepository {

    override suspend fun getMainCategories(): ApiResponse<List<EachCategoryResponse>> {
        return remoteDataSource.getMainCategories()
    }

    override suspend fun getSubCategories(mainCategoryId: Long): ApiResponse<List<EachCategoryResponse>> {
        return remoteDataSource.getSubCategories(mainCategoryId)
    }
}
