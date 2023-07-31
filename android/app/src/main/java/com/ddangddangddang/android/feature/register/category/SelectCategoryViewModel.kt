package com.ddangddangddang.android.feature.register.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.mapper.CategoryModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.CategoryRepository

class SelectCategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()
    val event: LiveData<SelectCategoryEvent>
        get() = _event

    private val _mainCategories = MutableLiveData<List<CategoryModel>>(
        categoryRepository.getMainCategories().categories.map { it.toPresentation() }
            .toMutableList(),
    )
    val mainCategories: LiveData<List<CategoryModel>>
        get() = _mainCategories

    private var _subCategories = mutableListOf<CategoryModel>()
    val subCategories: List<CategoryModel>
        get() = _subCategories.toList()

    fun setExitEvent() {
        _event.value = SelectCategoryEvent.Exit
    }

    fun setMainCategorySelection(mainCategoryId: Long) {
        _mainCategories.value?.let { items ->
            _mainCategories.value = items.map { categoryModel -> // mainCategoryId로 isChecked = true, 나머지 false로 변경
                if (categoryModel.id == mainCategoryId) {
                    categoryModel.copy(isChecked = true)
                } else if (categoryModel.isChecked) {
                    categoryModel.copy(isChecked = false)
                } else {
                    categoryModel
                }
            }

            // 서브 카테고리 변경
            val newSubCategories =
                categoryRepository.getSubCategories(mainCategoryId).categories.map { it.toPresentation() }
            _subCategories = newSubCategories.toMutableList()

            _event.value = SelectCategoryEvent.MainCategoriesSelectionChanged(subCategories) // Adapter 갱신 위한 이벤트 변경
        }
    }

    fun setSubCategorySelection(subCategoryId: Long) {
        _subCategories.replaceAll {
            if (it.id == subCategoryId) {
                it.copy(isChecked = true)
            } else if (it.isChecked) {
                it.copy(isChecked = false)
            } else {
                it
            }
        }
        _event.value =
            SelectCategoryEvent.SubCategoriesSelectionChanged(subCategories) // Adapter 갱신 위한 이벤트 변경
    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
        data class MainCategoriesSelectionChanged(val subCategories: List<CategoryModel>) : SelectCategoryEvent()

        data class SubCategoriesSelectionChanged(val subCategories: List<CategoryModel>) :
            SelectCategoryEvent()
    }
}
