package com.ddangddangddang.android.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel(private val repository: AuctionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SearchEvent> = SingleLiveEvent()
    val event: LiveData<SearchEvent>
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

    private var _page: Int = 0

    private val _searchStatus: MutableLiveData<SearchStatus> =
        MutableLiveData(SearchStatus.BeforeSearch)
    val searchStatus: LiveData<SearchStatus>
        get() = _searchStatus

    fun loadAuctions() { // 무한 스크롤
        if (!_loadingAuctionInProgress) fetchAuctions(_page + 1)
    }

    fun submitKeyword() { // 검색
        if (!checkKeywordLimit()) return
        if (!_loadingAuctionInProgress) fetchAuctions(DEFAULT_PAGE)
    }

    private fun checkKeywordLimit(): Boolean {
        keyword.value?.let {
            if (it.length !in KEYWORD_LENGTH_RANGE_MIN..KEYWORD_LENGTH_RANGE_MAX) {
                _event.value = SearchEvent.KeywordLimit
                return false
            }
        }
        return true
    }

    private fun fetchAuctions(newPage: Int) {
        viewModelScope.launch {
            keyword.value?.let {
                _loadingAuctionInProgress = true
                when (
                    val response =
                        repository.getAuctionPreviewsByTitle(
                            page = newPage,
                            size = SIZE_AUCTION_LOAD,
                            title = it,
                        )
                ) {
                    is ApiResponse.Success -> {
                        updateAuctions(response.body)
                        _page = newPage
                    }

                    is ApiResponse.Failure -> {
                        _event.value = SearchEvent.LoadFailureNotice(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value = SearchEvent.LoadFailureNotice(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value = SearchEvent.LoadFailureNotice(ErrorType.UNEXPECTED)
                    }
                }
                _loadingAuctionInProgress = false

                if (newPage == DEFAULT_PAGE) changeStatus() // 첫 검색인 경우 검색 상태 변경
            }
        }
    }

    private fun changeStatus() {
        if (_auctions.value.isNullOrEmpty()) {
            _searchStatus.value = SearchStatus.NoData
            return
        }
        _searchStatus.value = SearchStatus.ExistData
    }

    private fun updateAuctions(response: AuctionPreviewsResponse) {
        _auctions.value?.let { items ->
            val newItems = response.auctions.map { it.toPresentation() }
            _auctions.value = if (_page == DEFAULT_PAGE) {
                newItems
            } else {
                items + newItems
            }
            _isLast = response.isLast
        }
    }

    sealed class SearchEvent {
        object KeywordLimit : SearchEvent()
        class LoadFailureNotice(val error: ErrorType) : SearchEvent()
    }

    sealed class SearchStatus {
        object BeforeSearch : SearchStatus()
        object NoData : SearchStatus()
        object ExistData : SearchStatus()
    }

    companion object {
        private const val SIZE_AUCTION_LOAD = 10
        private const val DEFAULT_PAGE = 1
        private const val KEYWORD_LENGTH_RANGE_MIN = 2
        private const val KEYWORD_LENGTH_RANGE_MAX = 20
    }
}
