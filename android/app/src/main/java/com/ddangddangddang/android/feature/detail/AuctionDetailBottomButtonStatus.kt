package com.ddangddangddang.android.feature.detail

import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.ChatAuctionDetailModel

sealed class AuctionDetailBottomButtonStatus(
    @StringRes val content: Int,
    val enabled: Boolean,
) {
    object AuctionBid : AuctionDetailBottomButtonStatus(R.string.detail_auction_submit, true)
    object AuctionBidFinish :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_finish, false)

    object AuctionChatCreatable :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_chat_room_create, true)

    data class AuctionChatEntrance(val chatId: Long) :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_chat_room_entrance, true)

    companion object {
        fun find(
            auctionStatus: AuctionDetailStatusModel,
            chatStatus: ChatAuctionDetailModel,
        ): AuctionDetailBottomButtonStatus {
            chatStatus.id?.let { if (chatStatus.isChatParticipant) return AuctionChatEntrance(it) }
            return when {
                chatStatus.isChatParticipant -> AuctionChatCreatable
                auctionStatus == AuctionDetailStatusModel.ONGOING || auctionStatus == AuctionDetailStatusModel.UNBIDDEN -> AuctionBid
                else -> AuctionBidFinish
            }
        }
    }
}
