package com.ddangddangddang.android.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentHomeBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.register.RegisterAuctionActivity
import com.ddangddangddang.android.util.binding.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }
    private val auctionScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (viewModel.isLast) return

            if (!viewModel.loadingAuctionInProgress) {
                val lastVisibleItemPosition =
                    (binding.rvAuction.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val auctionsSize = viewModel.auctions.value?.size ?: 0
                if (lastVisibleItemPosition + 5 >= auctionsSize) {
                    viewModel.loadAuctions()
                }
            }
        }
    }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        viewModel.navigateToAuctionDetail(auctionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupViewModel()
        setupAuctionRecyclerView()
        if (viewModel.lastAuctionId.value == null) {
            viewModel.loadAuctions()
        }
    }

    private fun setupViewModel() {
        viewModel.auctions.observe(viewLifecycleOwner) { auctionAdapter.setAuctions(it) }
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
    }

    private fun handleEvent(event: HomeViewModel.HomeEvent) {
        when (event) {
            is HomeViewModel.HomeEvent.NavigateToAuctionDetail -> {
                navigateToAuctionDetail(event.auctionId)
            }

            is HomeViewModel.HomeEvent.NavigateToRegisterAuction -> {
                navigateToRegisterAuction()
            }
        }
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        startActivity(intent)
    }

    private fun navigateToRegisterAuction() {
        val intent = RegisterAuctionActivity.getIntent(requireContext())
        startActivity(intent)
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvAuction) {
            adapter = auctionAdapter
            addItemDecoration(AuctionSpaceItemDecoration(spanCount = 2, space = 20))
            addOnScrollListener(auctionScrollListener)
        }
    }
}
