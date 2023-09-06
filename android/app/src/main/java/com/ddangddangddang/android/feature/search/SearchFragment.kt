package com.ddangddangddang.android.feature.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentSearchBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.android.util.binding.BindingFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        navigateToAuctionDetail(auctionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAuctionRecyclerView()
        setupViewModel()
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
        auctionAdapter.submitList(auctions)
        Log.d("test", "Click")
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearchKeyword.windowToken, 0)
    }

    private fun setupAuctionRecyclerView() {
        binding.rvSearchAuctions.adapter = auctionAdapter.apply {
            submitList(
                listOf(
                    AuctionHomeModel(0, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
                    AuctionHomeModel(1, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
                    AuctionHomeModel(2, "ihi", "", 1000, AuctionHomeStatusModel.SUCCESS, 0),
                ),
            )
        }
        binding.rvSearchAuctions.addItemDecoration(AuctionSpaceItemDecoration(2, 20))
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}
