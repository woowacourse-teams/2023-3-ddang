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
        setupDecoration()
        setupAdapter()
        setupObserve()
        viewModel.loadMainCategories()
    }

    private fun setupDecoration() {
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvMainCategory.addItemDecoration(dividerItemDecoration)
    }

    private fun setupAdapter() {
        // Main
        binding.rvMainCategory.adapter = mainAdapter

        // Sub
        binding.rvSubCategory.adapter = subAdapter
    }

    private fun setupObserve() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
        viewModel.mainCategories.observe(this) {
            mainAdapter.setCategories(it)
        }
        viewModel.subCategories.observe(this) {
            subAdapter.setCategories(it)
        }
    }

    private fun handleEvent(event: SelectCategoryViewModel.SelectCategoryEvent) {
        when (event) {
            is SelectCategoryViewModel.SelectCategoryEvent.Exit -> finish()
            is SelectCategoryViewModel.SelectCategoryEvent.MainCategoriesSelectionChanged -> {
                subAdapter.setCategories(event.subCategories) // 목록 변경
            }

            is SelectCategoryViewModel.SelectCategoryEvent.SubCategoriesSelectionChanged -> {
                subAdapter.setCategories(event.subCategories) // 서브 카테고리 selected 변경
                // 인텐트에 결과값 담아서 등록 페이지로 리턴하는 코드 들어갈 예정
            }
        }
    }
}
