package com.ddangddangddang.android.feature.myAuction

import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMyAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.util.binding.BindingActivity

class MyAuctionActivity : BindingActivity<ActivityMyAuctionBinding>(R.layout.activity_my_auction) {
    private val viewModel: MyAuctionViewModel by viewModels { viewModelFactory }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        viewModel.navigateToAuctionDetail(auctionId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        if (viewModel.auctions.value == null) viewModel.loadMyAuctions()
        setupViewMdoel()
        setupAuctionRecyclerView()
    }

    private fun setupViewMdoel() {
        viewModel.auctions.observe(this) {
            auctionAdapter.setAuctions(it)
        }
        viewModel.event.observe(this) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: MyAuctionViewModel.Event) {
        when (event) {
            MyAuctionViewModel.Event.Exit -> finish()
        }
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvMyAuction) {
            adapter = auctionAdapter

            val space = resources.getDimensionPixelSize(R.dimen.margin_side_layout)
            addItemDecoration(AuctionSpaceItemDecoration(spanCount = 2, space = space))
        }
    }
}
