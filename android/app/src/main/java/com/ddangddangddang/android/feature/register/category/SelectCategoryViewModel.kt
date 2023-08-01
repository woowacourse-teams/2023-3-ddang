package com.ddangddangddang.android.feature.register.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.mapper.CategoryModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class SelectCategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()
    val event: LiveData<SelectCategoryEvent>
        get() = _event

    private val _mainCategories = MutableLiveData<List<CategoryModel>>()
    val mainCategories: LiveData<List<CategoryModel>>
        get() = _mainCategories

    private val _subCategories = MutableLiveData<List<CategoryModel>>()
    val subCategories: LiveData<List<CategoryModel>>
        get() = _subCategories

    private val subCategoriesCache = mutableMapOf<Long, List<CategoryModel>>()

    fun loadMainCategories() {
        viewModelScope.launch {
            val response = categoryRepository.getMainCategories()
            when (response) {
                is ApiResponse.Success -> {
                    val presentation = response.body.map { it.toPresentation() }
                    _mainCategories.value = presentation
                    presentation.forEach {
                        subCategoriesCache[it.id] = emptyList()
                    }
                }
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun setExitEvent() {
        _event.value = SelectCategoryEvent.Exit
    }

    fun setMainCategorySelection(mainCategoryId: Long) {
        _mainCategories.value?.let { items ->
            _mainCategories.value = items.changeIsChecked(mainCategoryId)

            // 서브 카테고리 변경
            if (subCategoriesCache[mainCategoryId] == null || subCategoriesCache[mainCategoryId]!!.isEmpty()) {
                viewModelScope.launch {
                    val response = categoryRepository.getSubCategories(mainCategoryId)
                    when (response) {
                        is ApiResponse.Success -> {
                            val presentation = response.body.map { it.toPresentation() }
                            _subCategories.value = presentation
                        }
                        is ApiResponse.Failure -> {}
                        is ApiResponse.NetworkError -> {}
                        is ApiResponse.Unexpected -> {}
                    }
                }
            } else {
                _subCategories.value = subCategoriesCache[mainCategoryId]
            }
        }
    }

    fun submitCategory(subCategoryId: Long) {
        val main = _mainCategories.value?.find { it.isChecked }
        val sub = _subCategories.value?.find { it.id == subCategoryId }

        if (main != null && sub != null) {
            val category = CategoryModel("${main.name} > ${sub.name}", subCategoryId)
            _event.value = SelectCategoryEvent.Submit(category)
        }
    }

    private fun List<CategoryModel>.changeIsChecked(checkedId: Long): List<CategoryModel> =
        this.map {
            if (it.id == checkedId) {
                it.copy(isChecked = true)
            } else {
                it.copy(isChecked = false)
            }
        }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
        data class Submit(val category: CategoryModel) : SelectCategoryEvent()
    }
}
