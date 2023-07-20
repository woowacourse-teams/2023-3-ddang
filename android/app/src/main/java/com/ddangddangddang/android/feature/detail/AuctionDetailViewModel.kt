package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.mapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.AuctionDetailResponse
import com.ddangddangddang.data.model.AuctionResponse
import com.ddangddangddang.data.model.CategoryResponse
import com.ddangddangddang.data.model.RegionResponse
import com.ddangddangddang.data.model.SellerResponse
import kotlinx.coroutines.launch

class AuctionDetailViewModel : ViewModel() {
    private val _event: SingleLiveEvent<AuctionDetailEvent> = SingleLiveEvent()
    val event: LiveData<AuctionDetailEvent>
        get() = _event

    private val _auctionDetailModel: MutableLiveData<AuctionDetailModel> = MutableLiveData()
    val auctionDetailModel: LiveData<AuctionDetailModel>
        get() = _auctionDetailModel

    fun loadAuctionDetailInfo(auctionId: Long) {
        viewModelScope.launch {
            val response = AuctionDetailResponse(
                AuctionResponse(
                    1L,
                    images,
                    "맥북 M1 에어",
                    CategoryResponse("가전", "노트북"),
                    "잠깐 쓰고 구석에 짱박아둔 맥북 에어 올려봅니다.\n" +
                        "상태 S급이고 사이클 50 이하입니다.",
                    100000,
                    400000,
                    "ongoing",
                    1000,
                    "2023-07-18T20:30:00",
                    "2023-07-20T20:30:00",
                    listOf(RegionResponse("서울특별시", "", ""), RegionResponse("경기도", "시흥시", "")),
                    25,
                ),
                SellerResponse(
                    1L,
                    "https://i.namu.wiki/i/lIPR6OQ_uFmBly_SxoV46LIZ8FhyIB7-wDb5YDCPxk8uu1GVfuxRlGpIIkX-AFBJw6HBigp_58nnjmkl8PjTdVYVmBBplSEJ-piK1-3DQ5w3CIqdo_2ioRYIOqigI7ddnjDu-iUHNKs4NVLAz6Qa7A.webp",
                    "글로",
                    0.5,
                ),
            ).toPresentation()

            _auctionDetailModel.value = response
        }
    }

    val images = listOf(
        "https://i0.wp.com/opensea.kr/wp-content/uploads/2021/01/IMG_1767.jpeg?w=900&ssl=1",
        "https://i0.wp.com/opensea.kr/wp-content/uploads/2021/01/IMG_1794.jpeg?w=900&ssl=1",
        "https://i0.wp.com/opensea.kr/wp-content/uploads/2021/01/IMG_1781-1.jpeg?w=900&ssl=1",
    )

    fun exit() {
        _event.value = AuctionDetailEvent.Exit
    }

    sealed class AuctionDetailEvent {
        object Exit : AuctionDetailEvent()
    }
}
