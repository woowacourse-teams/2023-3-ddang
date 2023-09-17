package com.ddangddangddang.android.feature.myAuction

import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMyAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class MyAuctionActivity : BindingActivity<ActivityMyAuctionBinding>(R.layout.activity_my_auction) {
    private val viewModel: MyAuctionViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupViewMdoel()
    }

    private fun setupViewMdoel() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: MyAuctionViewModel.Event) {
        when (event) {
            MyAuctionViewModel.Event.Exit -> finish()
        }
    }
}
