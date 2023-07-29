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
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentAuctionBidDialogBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel

class AuctionBidDialog : DialogFragment() {
    private var _binding: FragmentAuctionBidDialogBinding? = null
    private val binding: FragmentAuctionBidDialogBinding
        get() = _binding!!

    private val activityViewModel: AuctionDetailViewModel by activityViewModels { viewModelFactory }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let { formatNumber(s) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuctionBidDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.activityViewModel = activityViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.etBidPrice.addTextChangedListener(watcher)
        binding.etBidPrice.setOnClickListener {
            val curLength = binding.etBidPrice.text.toString().length
            binding.etBidPrice.setSelection(curLength - 2)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etBidPrice.requestFocus()
        binding.etBidPrice.setSelection(binding.etBidPrice.text.toString().length - 2)
    }

    private fun formatNumber(editable: Editable) {
        val originalValue = editable.toString().replace(",", "") // 문자열 내 들어있는 콤마를 모두 제거
        val targetString = " 원"
        val priceValue =
            originalValue.substringBefore(targetString) // 앞에서부터 가장 먼저 " 원"이라는 글자가 나오면 그 앞까지만 반환. 만약 없으면, 기존 문자열 그대로 반환.
        val parsedValue = priceValue.toIntOrNull() ?: getDefaultPrice(priceValue)

        if (parsedValue > Int.MAX_VALUE) return setInputPrice(Int.MAX_VALUE)
        setInputPrice(parsedValue)
    }

    private fun setInputPrice(price: Int) {
        val displayPrice = getString(R.string.detail_auction_bid_dialog_input_price).format(price)
        binding.etBidPrice.removeTextChangedListener(watcher)
        binding.etBidPrice.setText(displayPrice)
        binding.etBidPrice.setSelection(displayPrice.length - 2) // " 원" 앞으로 커서 이동
        binding.etBidPrice.addTextChangedListener(watcher)
    }

    private fun getDefaultPrice(priceValue: String): Int =
        if ((priceValue.toBigIntegerOrNull()?.compareTo(Int.MAX_VALUE.toBigInteger()) ?: -1) == 1) {
            Int.MAX_VALUE
        } else {
            0
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
