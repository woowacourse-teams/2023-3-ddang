package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.CategoryRemoteDataSource
import com.ddangddangddang.data.model.response.EachCategoryResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
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

    companion object {
        @Volatile
        private var instance: CategoryRepositoryImpl? = null

        fun getInstance(service: AuctionService): CategoryRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: AuctionService): CategoryRepositoryImpl {
            return CategoryRepositoryImpl(CategoryRemoteDataSource(service)).also { instance = it }
        }
    }
}
