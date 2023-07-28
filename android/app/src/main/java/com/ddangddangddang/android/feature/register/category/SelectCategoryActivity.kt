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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvMainCategory.addItemDecoration(dividerItemDecoration)
        binding.rvMainCategory.adapter =
            MainCategoryAdapter(listOf("가전제품", "가전제품", "가전제품", "가전제품", "가전제품", "가전제품"))
        binding.rvSubCategory.adapter =
            SubCategoryAdapter(listOf("컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터"))
    }
}
