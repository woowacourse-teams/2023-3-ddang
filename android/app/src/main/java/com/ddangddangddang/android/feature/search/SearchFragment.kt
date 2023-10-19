package com.ddangddangddang.android.feature.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentSearchBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.util.binding.BindingFragment
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private val auctionAdapter = AuctionAdapter { auctionId ->
        navigateToAuctionDetail(auctionId)
    }
    private val auctionScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (viewModel.isLast) return

            if (!viewModel.loadingAuctionInProgress) {
                val lastVisibleItemPosition =
                    (binding.rvSearchAuctions.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val auctionsSize = viewModel.auctions.value?.size ?: 0
                if (lastVisibleItemPosition + 10 >= auctionsSize) {
                    viewModel.loadAuctions()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAuctionRecyclerView()
        setupKeyboard()
        setupViewModel()
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvSearchAuctions) {
            adapter = auctionAdapter
            setHasFixedSize(true)
            addItemDecoration(
                AuctionSpaceItemDecoration(
                    2,
                    resources.getDimensionPixelSize(R.dimen.margin_side_layout),
                ),
            )
            addOnScrollListener(auctionScrollListener)
        }
    }

    private fun setupKeyboard() {
        binding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.submitKeyword()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        viewModel.auctions.observe(viewLifecycleOwner) {
            changeAuctions(it)
        }
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                SearchViewModel.SearchEvent.KeywordLimit -> notifyFailureMessage(
                    ErrorType.FAILURE(getString(R.string.search_notice_keyword_limit)),
                )

                is SearchViewModel.SearchEvent.LoadFailureNotice -> notifyFailureMessage(event.error)
            }
        }
    }

    private fun changeAuctions(auctions: List<AuctionHomeModel>) {
        auctionAdapter.submitList(auctions)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearchKeyword.windowToken, 0)
    }

    private fun notifyFailureMessage(type: ErrorType) {
        Toaster.showShort(
            requireContext(),
            type.message ?: getString(R.string.search_notice_default_error),
        )
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}
