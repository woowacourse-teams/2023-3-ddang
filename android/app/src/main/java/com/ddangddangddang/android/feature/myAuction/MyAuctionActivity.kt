package com.ddangddangddang.android.feature.myAuction

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMyAuctionBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showSnackbar

class MyAuctionActivity : BindingActivity<ActivityMyAuctionBinding>(R.layout.activity_my_auction) {
    private val viewModel: MyAuctionViewModel by viewModels { viewModelFactory }
    private val auctionScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (viewModel.isLast.value == true) return

            if (!viewModel.loadingAuctionInProgress) {
                val lastVisibleItemPosition =
                    (binding.rvMyAuction.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val auctionsSize = viewModel.auctions.value?.size ?: 0
                if (lastVisibleItemPosition + 5 >= auctionsSize) {
                    viewModel.loadMyAuctions()
                }
            }
        }
    }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        viewModel.navigateToAuctionDetail(auctionId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        if (viewModel.page == 0) viewModel.loadMyAuctions()
        setupViewMdoel()
        setupAuctionRecyclerView()
        setupReloadAuctions()
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
            is MyAuctionViewModel.Event.Exit -> finish()
            is MyAuctionViewModel.Event.NavigateToAuctionDetail -> {
                navigateToAuctionDetail(event.auctionId)
            }

            is MyAuctionViewModel.Event.FailureLoadEvent -> {
                handleErrorEvent(event.type)
            }
        }
    }

    private fun handleErrorEvent(errorType: ErrorType) {
        val defaultMessage = getString(R.string.my_auction_load_failed_title)
        val actionMessage = getString(R.string.all_snackbar_default_action)
        val message = when (errorType) {
            is ErrorType.FAILURE -> errorType.message
            is ErrorType.NETWORK_ERROR -> getString(errorType.messageId)
            is ErrorType.UNEXPECTED -> getString(errorType.messageId)
        }
        binding.root.showSnackbar(
            message = message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(this, auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvMyAuction) {
            adapter = auctionAdapter

            val space = resources.getDimensionPixelSize(R.dimen.margin_side_layout)
            addItemDecoration(AuctionSpaceItemDecoration(spanCount = 2, space = space))
            addOnScrollListener(auctionScrollListener)
        }
    }

    private fun setupReloadAuctions() {
        binding.srlReloadAuctions.setOnRefreshListener {
            viewModel.reloadAuctions()
            binding.srlReloadAuctions.isRefreshing = false
        }
    }
}
