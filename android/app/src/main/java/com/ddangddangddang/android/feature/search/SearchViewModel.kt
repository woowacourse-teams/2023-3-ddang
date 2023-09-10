package com.ddangddangddang.android.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import com.ddangddangddang.android.feature.search.SearchViewModel.SearchEvent as SearchEvent1

class SearchViewModel(private val repository: AuctionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SearchEvent1> = SingleLiveEvent()
    val event: LiveData<SearchEvent1>
        get() = _event

    val keyword: MutableLiveData<String> = MutableLiveData("")

    private val _auctions: MutableLiveData<List<AuctionHomeModel>> = MutableLiveData(emptyList())
    val auctions: LiveData<List<AuctionHomeModel>>
        get() = _auctions

    private var _loadingAuctionInProgress = false
    val loadingAuctionInProgress: Boolean
        get() = _loadingAuctionInProgress

    private var _isLast = false
    val isLast: Boolean
        get() = _isLast

    private var _lastAuctionId: Long? = null
    val lastAuctionId: Long?
        get() = _lastAuctionId

    private val _searchStatus: MutableLiveData<SearchStatus> =
        MutableLiveData(SearchStatus.BeforeSearch)
    val searchStatus: LiveData<SearchStatus>
        get() = _searchStatus

    fun submitKeyword() {
        if (!checkKeywordLimit()) return
        if (!_loadingAuctionInProgress) {
            _loadingAuctionInProgress = true
            viewModelScope.launch {
                keyword.value?.let {
                    when (
                        val response =
                            repository.getAuctionPreviews(_lastAuctionId, SIZE_AUCTION_LOAD, it)
                    ) {
                        is ApiResponse.Success -> {
                            initAuctions()
                            updateAuctions(response)
                        }
                        is ApiResponse.Failure -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.error)
                        }
                        is ApiResponse.NetworkError -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.exception.message)
                        }
                        is ApiResponse.Unexpected -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.t?.message)
                        }
                    }
                    _loadingAuctionInProgress = false
                }
            }
        }

        if (_auctions.value.isNullOrEmpty()) {
            _searchStatus.value = SearchStatus.NoData
            return
        }
        _searchStatus.value = SearchStatus.ExistData
    }

    private fun checkKeywordLimit(): Boolean {
        keyword.value?.let {
            if (it.length !in 2..20) {
                _event.value = SearchEvent.KeywordLimit
                return false
            }
        }
        return true
    }

    private fun initAuctions() {
        _lastAuctionId = null
        _auctions.value = emptyList()
    }

    private fun updateAuctions(response: ApiResponse.Success<AuctionPreviewsResponse>) {
        _auctions.value?.let { items ->
            _auctions.value = items + response.body.auctions.map { it.toPresentation() }
            _isLast = response.body.isLast
            _lastAuctionId = response.body.auctions.maxOf { it.id }
        }
    }

    fun loadAuctions() {
        // repository에서 검색 결과 List를 가져오는 코드
        if (!_loadingAuctionInProgress) {
            _loadingAuctionInProgress = true
            viewModelScope.launch {
                keyword.value?.let {
                    when (
                        val response =
                            repository.getAuctionPreviews(_lastAuctionId, SIZE_AUCTION_LOAD, it)
                    ) {
                        is ApiResponse.Success -> {
                            updateAuctions(response)
                        }

                        is ApiResponse.Failure -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.error)
                        }
                        is ApiResponse.NetworkError -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.exception.message)
                        }
                        is ApiResponse.Unexpected -> {
                            _event.value = SearchEvent.LoadFailureNotice(response.t?.message)
                        }
                    }
                    _loadingAuctionInProgress = false
                }
            }
        }
    }

    sealed class SearchEvent {
        object KeywordLimit : SearchEvent()
        class LoadFailureNotice(val message: String?) : SearchEvent()
    }

    sealed class SearchStatus {
        object BeforeSearch : SearchStatus()
        object NoData : SearchStatus()
        object ExistData : SearchStatus()
    }

    companion object {
        private const val SIZE_AUCTION_LOAD = 10
    }
}
