package com.ddangddangddang.android.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class SearchViewModel : ViewModel() {
    private val _event: SingleLiveEvent<SearchEvent> = SingleLiveEvent()
    val event: LiveData<SearchEvent>
        get() = _event

    val keyword: MutableLiveData<String> = MutableLiveData("")

    fun submitKeyword() {
        // repository에서 검색 결과 List를 가져오는 코드
        _event.value = SearchEvent.KeywordSubmit(listOf())
    }

    sealed class SearchEvent {
        data class KeywordSubmit(val auctions: List<AuctionHomeModel>) : SearchEvent()
    }
}
