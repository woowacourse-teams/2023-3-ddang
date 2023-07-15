package com.ddangddangddang.android.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMainBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.home.HomeFragment
import com.ddangddangddang.android.feature.message.MessageFragment
import com.ddangddangddang.android.feature.mypage.MyPageFragment
import com.ddangddangddang.android.feature.search.SearchFragment
import com.ddangddangddang.android.util.binding.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        setupViewModel()
    }

    fun setupViewModel() {
        viewModel.currentFragmentType.observe(this) {
            changeFragment(it)
        }
    }

    private fun changeFragment(type: FragmentType) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)

            supportFragmentManager.fragments.forEach(::hide)

            supportFragmentManager.findFragmentByTag(type.tag)?.let {
                show(it)
            } ?: createFragment(type).run {
                add(R.id.fcv_container, this, type.tag)
            }
        }
    }

    private fun createFragment(type: FragmentType): Fragment {
        return when (type) {
            FragmentType.HOME -> HomeFragment()
            FragmentType.SEARCH -> SearchFragment()
            FragmentType.MESSAGE -> MessageFragment()
            FragmentType.MY_PAGE -> MyPageFragment()
        }
    }
}
