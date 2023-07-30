package com.ddangddangddang.android.feature.detail.bid

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentAuctionBidDialogBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel

class AuctionBidDialog : DialogFragment() {
    private var _binding: FragmentAuctionBidDialogBinding? = null
    private val binding: FragmentAuctionBidDialogBinding
        get() = _binding!!

    private val viewModel: AuctionBidViewModel by viewModels { viewModelFactory }
    private val activityViewModel: AuctionDetailViewModel by activityViewModels { viewModelFactory }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let { viewModel.changeInputPriceText(s.toString()) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuctionBidDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.activityViewModel = activityViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupListener()
        setupObserver()
    }

    private fun setupListener() {
        binding.etBidPrice.addTextChangedListener(watcher)
        binding.etBidPrice.setOnClickListener {
            val curLength = binding.etBidPrice.text.toString().length
            binding.etBidPrice.setSelection(curLength - 2)
        }

        binding.tvBidCancel.setOnClickListener {
            activityViewModel.loadAuctionDetail(2L)
        }
    }

    private fun setupObserver() {
        viewModel.bidPrice.observe(viewLifecycleOwner) {
            setInputBidPrice(it)
        }

        activityViewModel.auctionDetailModel.observe(viewLifecycleOwner) {
            viewModel.setBidPrice(it.lastBidPrice + it.bidUnit)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etBidPrice.requestFocus()
        binding.etBidPrice.setSelection(binding.etBidPrice.text.toString().length - 2)
    }

    private fun setInputBidPrice(price: Int) {
        val displayPrice = getString(R.string.detail_auction_bid_dialog_input_price).format(price)
        binding.etBidPrice.removeTextChangedListener(watcher)
        binding.etBidPrice.setText(displayPrice)
        binding.etBidPrice.setSelection(displayPrice.length - 2) // " 원" 앞으로 커서 이동
        binding.etBidPrice.addTextChangedListener(watcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
