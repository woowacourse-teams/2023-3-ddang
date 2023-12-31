package com.ddangddangddang.android.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentHomeBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.register.RegisterAuctionActivity
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private val auctionScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (viewModel.isLast) return

            if (!viewModel.loadingAuctionInProgress) {
                val lastVisibleItemPosition =
                    (binding.rvAuction.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val auctionsSize = viewModel.auctions.value?.size ?: 0
                if (lastVisibleItemPosition + 10 >= auctionsSize) {
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
        if (viewModel.page == 0) viewModel.loadAuctions()
        setupReloadAuctions()
    }

    private fun setupViewModel() {
        viewModel.auctions.observe(viewLifecycleOwner) {
            auctionAdapter.setAuctions(it) {
                if (viewModel.page == 1) binding.rvAuction.scrollToPosition(0)
            }
        }
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

            is HomeViewModel.HomeEvent.FailureLoadAuctions -> {
                requireActivity().notifyFailureMessage(
                    event.errorType,
                    R.string.home_default_error_message,
                )
            }
        }
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun navigateToRegisterAuction() {
        val intent = RegisterAuctionActivity.getIntent(requireContext())
        startActivity(intent)
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvAuction) {
            adapter = auctionAdapter
            setHasFixedSize(true)

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

    fun scrollToTop() {
        val position =
            (binding.rvAuction.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
        if (position < 30) {
            binding.rvAuction.smoothScrollToPosition(0)
        } else {
            binding.rvAuction.scrollToPosition(0)
        }
    }
}
