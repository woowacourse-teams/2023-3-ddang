package com.ddangddangddang.android.feature.detail.bid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuctionBidViewModel : ViewModel() {
    private val _bidPrice: MutableLiveData<Int> = MutableLiveData()
    val bidPrice: LiveData<Int>
        get() = _bidPrice

    fun setBidPrice(price: Int) {
        _bidPrice.value = price
    }

    fun changeInputPriceText(string: String) {
        val originalValue = string.replace(",", "") // 문자열 내 들어있는 콤마를 모두 제거
        val targetString = SUFFIX_INPUT_PRICE // " 원"
        val priceValue = originalValue.substringBefore(targetString)
        val parsedValue = priceValue.toIntOrNull() ?: getDefaultPrice(priceValue)

        if (parsedValue > Int.MAX_VALUE) return setBidPrice(Int.MAX_VALUE)
        setBidPrice(parsedValue)
    }

    private fun getDefaultPrice(priceValue: String): Int =
        if ((priceValue.toBigIntegerOrNull()?.compareTo(Int.MAX_VALUE.toBigInteger()) ?: -1) == 1) {
            Int.MAX_VALUE
        } else {
            0
        }

    companion object {
        private const val SUFFIX_INPUT_PRICE = " 원"
    }
}
