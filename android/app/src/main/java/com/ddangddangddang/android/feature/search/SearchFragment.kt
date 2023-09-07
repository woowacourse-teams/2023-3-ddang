package com.ddangddangddang.android.feature.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentSearchBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.util.binding.BindingFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        navigateToAuctionDetail(auctionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAuctionRecyclerView()
        setupKeyboard()
        setupViewModel()
    }

    private fun setupAuctionRecyclerView() {
        binding.rvSearchAuctions.adapter = auctionAdapter
        binding.rvSearchAuctions.addItemDecoration(
            AuctionSpaceItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.margin_side_layout),
            ),
        )
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
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SearchViewModel.SearchEvent.KeywordSubmit -> changeAuctions(event.auctions)
            }
        }
    }

    private fun changeAuctions(auctions: List<AuctionHomeModel>) {
        hideKeyboard()
        hideNoticeInputKeyword()
        auctionAdapter.submitList(auctions)

        if (auctions.isEmpty()) {
            showNoticeNoAuctions()
            return
        }
        showAuctions()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearchKeyword.windowToken, 0)
    }

    private fun hideNoticeInputKeyword() {
        if (binding.tvNoticeInputKeyword.visibility == View.VISIBLE) {
            binding.tvNoticeInputKeyword.visibility = View.INVISIBLE
        }
    }

    private fun showNoticeNoAuctions() {
        binding.tvNoticeNoAuctions.visibility = View.VISIBLE
        binding.rvSearchAuctions.visibility = View.INVISIBLE
    }

    private fun showAuctions() {
        binding.tvNoticeNoAuctions.visibility = View.INVISIBLE
        binding.rvSearchAuctions.visibility = View.VISIBLE
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}
