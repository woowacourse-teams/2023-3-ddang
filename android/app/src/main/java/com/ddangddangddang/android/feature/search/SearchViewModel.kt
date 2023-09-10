package com.ddangddangddang.android.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class SearchViewModel : ViewModel() {
    private val _event: SingleLiveEvent<SearchEvent> = SingleLiveEvent()
    val event: LiveData<SearchEvent>
        get() = _event

    val keyword: MutableLiveData<String> = MutableLiveData("")

    private val _auctions: MutableLiveData<List<AuctionHomeModel>> = MutableLiveData()
    val auctions: LiveData<List<AuctionHomeModel>>
        get() = _auctions

    fun submitKeyword() {
        // repository에서 검색 결과 List를 가져오는 코드
        val dummy = listOf(
            AuctionHomeModel(0, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
            AuctionHomeModel(1, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
            AuctionHomeModel(2, "ihi", "", 1000, AuctionHomeStatusModel.SUCCESS, 0),
        )

        _auctions.value = dummy
    }

    sealed class SearchEvent {
    }
}
