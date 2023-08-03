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
import com.ddangddangddang.android.util.view.Toaster

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.bidPrice.value == null) {
            if (activityViewModel.minBidPrice == 0) return exit()
            viewModel.setBidPrice(activityViewModel.minBidPrice)
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

    override fun onResume() {
        super.onResume()
        binding.etBidPrice.requestFocus()
    }

    private fun setupListener() {
        binding.etBidPrice.addTextChangedListener(watcher)
        binding.etBidPrice.setOnClickListener {
            binding.etBidPrice.setSelection(getCursorPositionFrontSuffix(binding.etBidPrice.text.toString()))
        }
    }

    private fun setupObserver() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        viewModel.bidPrice.observe(viewLifecycleOwner) { setInputBidPrice(it) }
    }

    private fun handleEvent(event: AuctionBidViewModel.AuctionBidEvent) {
        when (event) {
            is AuctionBidViewModel.AuctionBidEvent.Cancel -> exit()
            is AuctionBidViewModel.AuctionBidEvent.SubmitSuccess -> submitSuccess(event.price)
            is AuctionBidViewModel.AuctionBidEvent.SubmitFailureEvent -> handleSubmitFailureEvent(event)
        }
    }

    private fun submitSuccess(price: Int) {
        showMessage(getString(R.string.detail_auction_bid_dialog_success, price))
        exit()
    }

    private fun handleSubmitFailureEvent(event: AuctionBidViewModel.AuctionBidEvent.SubmitFailureEvent) {
        showMessage(getString(event.messageId))
        exit()
    }

    private fun exit() {
        activityViewModel.auctionDetailModel.value?.let { activityViewModel.loadAuctionDetail(it.id) }
        dismiss()
    }

    private fun setInputBidPrice(price: Int) {
        val displayPrice = getString(R.string.detail_auction_bid_dialog_input_price, price)
        binding.etBidPrice.removeTextChangedListener(watcher)
        binding.etBidPrice.setText(displayPrice)
        binding.etBidPrice.setSelection(getCursorPositionFrontSuffix(displayPrice)) // " 원" 앞으로 커서 이동
        binding.etBidPrice.addTextChangedListener(watcher)
    }

    private fun getCursorPositionFrontSuffix(content: String): Int {
        return content.length - AuctionBidViewModel.SUFFIX_INPUT_PRICE.length
    }

    private fun showMessage(message: String) {
        Toaster.showShort(requireContext(), message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
