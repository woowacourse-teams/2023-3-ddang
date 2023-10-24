package com.ddangddangddang.android.feature.detail.bid

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentAuctionBidDialogBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.common.PriceTextWatcher
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.feature.register.RegisterAuctionViewModel
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuctionBidDialog : DialogFragment() {
    private var _binding: FragmentAuctionBidDialogBinding? = null
    private val binding: FragmentAuctionBidDialogBinding
        get() = _binding!!

    private val viewModel: AuctionBidViewModel by viewModels()
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()
    private val bidPriceWatcher by lazy { PriceTextWatcher { viewModel.changeInputPriceText(it) } }

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

        setupKeyboard()
        setupListener()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        binding.etBidPrice.requestFocus()
    }

    private fun setupKeyboard() {
        binding.etBidPrice.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etBidPrice.windowToken, 0)
    }

    private fun setupListener() {
        binding.etBidPrice.addTextChangedListener(bidPriceWatcher)
    }

    private fun setupObserver() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        viewModel.bidPrice.observe(viewLifecycleOwner) { setInputBidPrice(it) }
    }

    private fun handleEvent(event: AuctionBidViewModel.AuctionBidEvent) {
        when (event) {
            is AuctionBidViewModel.AuctionBidEvent.Cancel -> exit()
            is AuctionBidViewModel.AuctionBidEvent.SubmitSuccess -> submitSuccess(event.price)
            is AuctionBidViewModel.AuctionBidEvent.UnderPrice -> notifyUnderPriceSubmitFailed()
            is AuctionBidViewModel.AuctionBidEvent.FailureSubmitEvent -> {
                handleSubmitFailureEvent(event.type)
            }
        }
    }

    private fun notifyUnderPriceSubmitFailed() {
        showMessage(getString(R.string.detail_auction_bid_dialog_toast_under_price))
    }

    private fun submitSuccess(price: Int) {
        showMessage(getString(R.string.detail_auction_bid_dialog_success, price))
        exit()
    }

    private fun handleSubmitFailureEvent(errorType: ErrorType) {
        val defaultMessage = getString(R.string.detail_auction_bid_dialog_failure_default_message)
        Toaster.showShort(requireContext(), errorType.message ?: defaultMessage)
        exit()
    }

    private fun exit() {
        activityViewModel.auctionDetailModel.value?.let { activityViewModel.loadAuctionDetail(it.id) }
        dismiss()
    }

    private fun setInputBidPrice(price: Int) {
        val displayPrice = getString(R.string.all_price, price)
        binding.etBidPrice.removeTextChangedListener(bidPriceWatcher)
        binding.etBidPrice.setText(displayPrice)
        binding.etBidPrice.setSelection(
            bidPriceWatcher.getCursorPosition(
                displayPrice.length,
                RegisterAuctionViewModel.SUFFIX_INPUT_PRICE.length,
            ),
        ) // 이전 커서 위치로 이동
        binding.etBidPrice.addTextChangedListener(bidPriceWatcher)
    }

    private fun showMessage(message: String) {
        Toaster.showShort(requireContext(), message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val BID_DIALOG_TAG = "bid_dialog_tag"

        fun show(fragmentManager: FragmentManager) {
            AuctionBidDialog().show(fragmentManager, BID_DIALOG_TAG)
        }
    }
}
