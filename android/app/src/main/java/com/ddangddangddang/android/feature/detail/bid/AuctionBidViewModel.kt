package com.ddangddangddang.android.feature.detail.bid

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.R
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
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
        val bidPrice = bidPrice.value ?: return setUnderPriceEvent()
        if (isBidAmountUnderMinimum(bidPrice, minBidPrice)) return setUnderPriceEvent()
        viewModelScope.launch {
            when (val response = repository.submitAuctionBid(auctionId, bidPrice)) {
                is ApiResponse.Success -> _event.value = AuctionBidEvent.SubmitSuccess(bidPrice)
                is ApiResponse.Failure -> {
                    val jsonObject = JSONObject(response.error)
                    val message = jsonObject.getString("message")
                    val errorType = SubmitBidFailureResponse.find(message)
                    if (errorType == SubmitBidFailureResponse.ELSE) {
                        _event.value = AuctionBidEvent.SubmitFailureCustomEvent(message)
                    } else {
                        handleSubmitBidFailure(errorType)
                    }
                }

                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    private fun isBidAmountUnderMinimum(bidPrice: Int, minBidPrice: Int): Boolean {
        return bidPrice < minBidPrice
    }

    private fun handleSubmitBidFailure(failure: SubmitBidFailureResponse) {
        when (failure) {
            SubmitBidFailureResponse.FINISH -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.Finish
            }

            SubmitBidFailureResponse.DELETED -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.Deleted
            }

            SubmitBidFailureResponse.EXIST_HIGHER_BIDDER -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.ExistHigherBidder
            }

            SubmitBidFailureResponse.ALREADY_HIGHEST_BIDDER -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.AlreadyHighestBidder
            }

            SubmitBidFailureResponse.SELLER_CAN_NOT_BID -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.SellerCanNotBid
            }

            SubmitBidFailureResponse.ELSE -> {
                _event.value = AuctionBidEvent.SubmitFailureEvent.Unknown
            }
        }
    }

    private fun setUnderPriceEvent() {
        _event.value = AuctionBidEvent.UnderPrice
    }

    sealed class AuctionBidEvent {
        object Cancel : AuctionBidEvent()
        object UnderPrice : AuctionBidEvent()
        data class SubmitSuccess(val price: Int) : AuctionBidEvent()
        data class SubmitFailureCustomEvent(val message: String) : AuctionBidEvent()
        sealed class SubmitFailureEvent(@StringRes val messageId: Int) : AuctionBidEvent() {
            object Finish : SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_finish)
            object Deleted : SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_deleted)
            object ExistHigherBidder :
                SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_already_exist_higher_bidder)

            object AlreadyHighestBidder :
                SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_already_highest_bidder)

            object SellerCanNotBid :
                SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_seller_can_not_bid)

            object Unknown : SubmitFailureEvent(R.string.detail_auction_bid_dialog_failure_else)
        }
    }

    companion object {
        const val SUFFIX_INPUT_PRICE = " 원"
        private const val ZERO = 0
        private const val MAX_PRICE = 2100000000
    }
}
