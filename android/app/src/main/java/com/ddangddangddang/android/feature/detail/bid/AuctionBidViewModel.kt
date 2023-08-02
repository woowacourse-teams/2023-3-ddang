package com.ddangddangddang.android.feature.detail.bid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import java.math.BigInteger

class AuctionBidViewModel(
    private val repository: AuctionRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<AuctionBidEvent> = SingleLiveEvent()
    val event: LiveData<AuctionBidEvent>
        get() = _event

    private val _bidPrice: MutableLiveData<Int> = MutableLiveData()
    val bidPrice: LiveData<Int>
        get() = _bidPrice

    fun setBidPrice(price: Int) {
        _bidPrice.value = price
    }

    fun changeInputPriceText(string: String) {
        val originalValue = string.replace(",", "") // 문자열 내 들어있는 콤마를 모두 제거
        val priceValue = originalValue.substringBefore(SUFFIX_INPUT_PRICE) // " 원"
        val parsedValue =
            priceValue.toBigIntegerOrNull() ?: return setBidPrice(ZERO) // 입력에 문자가 섞인 경우

        if (parsedValue.isOverMaxPrice()) return setBidPrice(MAX_PRICE)
        setBidPrice(parsedValue.toInt()) // 파싱에 성공한 금액으로 설정
    }

    private fun BigInteger.isOverMaxPrice(): Boolean {
        return this > MAX_PRICE.toBigInteger()
    }

    fun cancel() {
        _event.value = AuctionBidEvent.Cancel
    }

    fun submit(auctionId: Long, minBidPrice: Int) {
        val bidPrice = bidPrice.value ?: return
        if (isBidAmountUnderMinimum(bidPrice, minBidPrice)) return
        viewModelScope.launch {
            when (val response = repository.submitAuctionBid(auctionId, bidPrice)) {
                is ApiResponse.Success -> {
                    Log.d("mendel", "success")
                }

                is ApiResponse.Failure -> {
                    Log.d("mendel", "failure ${response.error}")
                }

                is ApiResponse.NetworkError -> {
                    Log.d("mendel", "networkerror")
                }

                is ApiResponse.Unexpected -> {
                    Log.d("mendel", "enexpected")
                }
            }
        }
    }

    private fun isBidAmountUnderMinimum(bidPrice: Int, minBidPrice: Int): Boolean {
        return bidPrice < minBidPrice
    }

    sealed class AuctionBidEvent {
        object Cancel : AuctionBidEvent()
        data class SuccessSubmit(val price: Int) : AuctionBidEvent()
    }

    companion object {
        const val SUFFIX_INPUT_PRICE = " 원"
        private const val ZERO = 0
        private const val MAX_PRICE = Int.MAX_VALUE
    }
}
