package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.CategoriesResponse
import com.ddangddangddang.data.model.response.EachCategoryResponse

class CategoryRepositoryMockImpl : CategoryRepository {
    private val categories: Map<EachCategoryResponse, List<EachCategoryResponse>> = mapOf(
        EachCategoryResponse("채소", 0) to
            listOf(
                EachCategoryResponse("친환경", 0),
                EachCategoryResponse("고구마, 감자, 당근", 1),
                EachCategoryResponse("시금치, 쌈채소, 나물", 2),
            ),
        EachCategoryResponse("와인, 위스키", 1) to
            listOf(
                EachCategoryResponse("레드 와인", 3),
                EachCategoryResponse("화이트 와인", 4),
                EachCategoryResponse("위스키", 5),
            ),
        EachCategoryResponse("가전제품", 2) to
            listOf(
                EachCategoryResponse("주방가전", 6),
                EachCategoryResponse("생활가전", 7),
                EachCategoryResponse("계절가전", 8),
            ),
    )

    override fun getMainCategories(): CategoriesResponse {
        return CategoriesResponse(categories.keys.toList())
    }

    override fun getSubCategories(mainCategoryId: Long): CategoriesResponse {
        val temp = categories.filterKeys { it.id == mainCategoryId }.toList()[0]
        return CategoriesResponse(temp.second)
    }
}
