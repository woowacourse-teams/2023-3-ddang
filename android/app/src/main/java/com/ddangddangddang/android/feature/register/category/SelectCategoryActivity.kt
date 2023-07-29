package com.ddangddangddang.android.feature.register.category

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectCategoryBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class SelectCategoryActivity :
    BindingActivity<ActivitySelectCategoryBinding>(R.layout.activity_select_category) {
    private val viewModel by viewModels<SelectCategoryViewModel> { viewModelFactory }
    private val mainAdapter by lazy {
        MainCategoryAdapter { id ->
            viewModel.setMainCategorySelection(id)
        }
    }
    private val subAdapter by lazy {
        SubCategoryAdapter { id ->
            viewModel.setSubCategorySelection(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvMainCategory.addItemDecoration(dividerItemDecoration)

        // Adapter setting
        // Main
        binding.rvMainCategory.adapter = mainAdapter
        mainAdapter.setCategories(viewModel.mainCategories)

        // Sub
        binding.rvSubCategory.adapter = subAdapter
        subAdapter.setCategories(viewModel.subCategories)

        // Observe
        viewModel.event.observe(this) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: SelectCategoryViewModel.SelectCategoryEvent) {
        when (event) {
            is SelectCategoryViewModel.SelectCategoryEvent.Exit -> finish()
            is SelectCategoryViewModel.SelectCategoryEvent.MainCategoriesSelectionChanged -> {
                mainAdapter.setCategories(viewModel.mainCategories) // 메인 카테고리 selected 변경
                subAdapter.setCategories(viewModel.subCategories) // 목록 변경
            }
            is SelectCategoryViewModel.SelectCategoryEvent.SubCategoriesSelectionChanged -> {
                subAdapter.setCategories(viewModel.subCategories) // 서브 카테고리 selected 변경
                // 인텐트에 결과값 담아서 등록 페이지로 리턴하는 코드 들어갈 예정
            }
        }
    }
}
