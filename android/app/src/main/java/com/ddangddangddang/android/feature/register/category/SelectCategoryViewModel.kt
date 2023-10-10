package com.ddangddangddang.android.feature.register.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.mapper.CategoryModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias MainId = Long

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()
    val event: LiveData<SelectCategoryEvent>
        get() = _event

    private val _mainCategories = MutableLiveData<List<CategoryModel>>()
    val mainCategories: LiveData<List<CategoryModel>>
        get() = _mainCategories

    private val _subCategories = MutableLiveData<List<CategoryModel>>()
    val subCategories: LiveData<List<CategoryModel>>
        get() = _subCategories

    private val subCategoriesCache = mutableMapOf<MainId, List<CategoryModel>>()

    fun loadMainCategories() {
        viewModelScope.launch {
            when (val response = categoryRepository.getMainCategories()) {
                is ApiResponse.Success -> {
                    val mainCategories = response.body.map { it.toPresentation() }
                    _mainCategories.value = mainCategories
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        SelectCategoryEvent.MainCategoriesLoadFailure(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        SelectCategoryEvent.MainCategoriesLoadFailure(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value =
                        SelectCategoryEvent.MainCategoriesLoadFailure(ErrorType.UNEXPECTED)
                }
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
            changeSubCategories(mainCategoryId)
        }
    }

    private fun changeSubCategories(mainCategoryId: Long) {
        if (!subCategoriesCache[mainCategoryId].isNullOrEmpty()) {
            _subCategories.value = subCategoriesCache[mainCategoryId]
            return
        }
        viewModelScope.launch {
            when (val response = categoryRepository.getSubCategories(mainCategoryId)) {
                is ApiResponse.Success -> {
                    val subCategories = response.body.map { it.toPresentation() }
                    _subCategories.value = subCategories
                    subCategoriesCache[mainCategoryId] = subCategories // 캐시 저장
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        SelectCategoryEvent.SubCategoriesLoadFailure(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        SelectCategoryEvent.SubCategoriesLoadFailure(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value =
                        SelectCategoryEvent.SubCategoriesLoadFailure(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun submitCategory(subCategoryId: Long) {
        val main = _mainCategories.value?.find { it.isChecked } ?: return
        val sub = _subCategories.value?.find { it.id == subCategoryId } ?: return

        val category = CategoryModel("${main.name} > ${sub.name}", subCategoryId)
        _event.value = SelectCategoryEvent.Submit(category)
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
        data class MainCategoriesLoadFailure(val error: ErrorType) : SelectCategoryEvent()
        data class SubCategoriesLoadFailure(val error: ErrorType) : SelectCategoryEvent()
    }
}
