package com.ddangddangddang.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse

class AuctionLocalDataSource {
    private val auctionPreviews: MutableLiveData<List<AuctionPreviewResponse>> = MutableLiveData()

    fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>> {
        return auctionPreviews
    }

    fun addAuctionPreviews(auctions: List<AuctionPreviewResponse>) {
        auctionPreviews.value = auctionPreviews.value?.plus(auctions) ?: auctions
    }

    fun addAuctionPreview(auction: AuctionPreviewResponse) {
        val auctions = auctionPreviews.value ?: emptyList()
        auctionPreviews.value = listOf(auction) + auctions
    }

    fun updateAuctionPreview(detailAuction: AuctionDetailResponse) {
        val auction = detailAuction.auction
        val currentList = auctionPreviews.value ?: emptyList()

        val updatedList = currentList.map {
            if (it.id == auction.id) {
                auction.toPreview()
            } else {
                it
            }
        }
        auctionPreviews.value = updatedList
    }
}
