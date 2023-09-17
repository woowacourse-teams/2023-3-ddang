package com.ddangddangddang.android.feature.participateAuction

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityParticipateAuctionBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.Toaster

class ParticipateAuctionActivity :
    BindingActivity<ActivityParticipateAuctionBinding>(R.layout.activity_participate_auction) {
    private val viewModel: ParticipateAuctionViewModel by viewModels { viewModelFactory }
    private val auctionScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (viewModel.isLast.value == true) return

            if (!viewModel.loadingAuctionInProgress) {
                val lastVisibleItemPosition =
                    (binding.rvMyParticipateAuction.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val auctionsSize = viewModel.auctions.value?.size ?: 0
                if (lastVisibleItemPosition + 5 >= auctionsSize) {
                    viewModel.loadMyParticipateAuctions()
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
        if (viewModel.page == 0) viewModel.loadMyParticipateAuctions()
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

    private fun handleEvent(event: ParticipateAuctionViewModel.Event) {
        when (event) {
            is ParticipateAuctionViewModel.Event.Exit -> finish()
            is ParticipateAuctionViewModel.Event.NavigateToAuctionDetail -> {
                navigateToAuctionDetail(event.auctionId)
            }

            is ParticipateAuctionViewModel.Event.FailureLoadEvent -> {
                handleErrorEvent(event.type)
            }
        }
    }

    private fun handleErrorEvent(errorType: ErrorType) {
        when (errorType) {
            is ErrorType.FAILURE -> {
                showErrorMessage(errorType.message)
            }

            is ErrorType.NETWORK_ERROR -> {
                showErrorMessage(getString(R.string.all_network_error_message))
            }

            is ErrorType.UNEXPECTED -> {
                showErrorMessage(getString(R.string.all_unexpected_error_message))
            }
        }
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(this, auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun showErrorMessage(message: String?) {
        Toaster.showShort(
            this,
            message ?: getString(R.string.home_default_error_message),
        )
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvMyParticipateAuction) {
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