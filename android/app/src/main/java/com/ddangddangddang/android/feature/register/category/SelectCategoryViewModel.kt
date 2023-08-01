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
            _mainCategories.value = items.map { categoryModel -> // mainCategoryId로 isChecked = true, 나머지 false로 변경
                if (categoryModel.id == mainCategoryId) {
                    categoryModel.copy(isChecked = true)
                } else {
                    categoryModel.copy(isChecked = false)
                }
            }

            // 서브 카테고리 변경
            if (subCategoriesCache[mainCategoryId] == null) {
                viewModelScope.launch {
                    val response = categoryRepository.getSubCategories(mainCategoryId)
                    when (response) {
                        is ApiResponse.Success -> {
                            val presentation = response.body.map { it.toPresentation() }
                            _subCategories.value = presentation
                            _event.value = SelectCategoryEvent.MainCategoriesSelectionChanged(presentation) // Adapter 갱신 위한 이벤트 변경
                        }
                        is ApiResponse.Failure -> {}
                        is ApiResponse.NetworkError -> {}
                        is ApiResponse.Unexpected -> {}
                    }
                }
            } else {
                _subCategories.value = subCategoriesCache[mainCategoryId]
                _event.value = SelectCategoryEvent.MainCategoriesSelectionChanged(subCategoriesCache[mainCategoryId] ?: emptyList())
            }
        }
    }

    fun setSubCategorySelection(subCategoryId: Long) {
        _subCategories.value?.let { items ->
            _subCategories.value = items.map { categoryModel ->
                if (categoryModel.id == subCategoryId) {
                    categoryModel.copy(isChecked = true)
                } else {
                    categoryModel.copy(isChecked = false)
                }
            }
//            _event.value =
//                SelectCategoryEvent.SubCategoriesSelectionChanged(_subCategories.value) // Adapter 갱신 위한 이벤트 변경
        }
    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
        data class MainCategoriesSelectionChanged(val subCategories: List<CategoryModel>) : SelectCategoryEvent()

        data class SubCategoriesSelectionChanged(val subCategories: List<CategoryModel>) :
            SelectCategoryEvent()
    }
}
