package com.ddangddangddang.android.feature.detail

import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel

sealed class AuctionDetailBottomButtonStatus(
    @StringRes val text: Int,
    val enabled: Boolean,
) {
    object BidAuction : AuctionDetailBottomButtonStatus(R.string.detail_auction_submit, true)

    object FinishAuction :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_finish, false)

    object CreateAuctionChatRoom :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_chat_room_create, true)

    object EnterAuctionChatRoom :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_chat_room_entrance, true)

    companion object {
        fun find(
            auctionDetailModel: AuctionDetailModel,
        ): AuctionDetailBottomButtonStatus {
            val auctionStatus = auctionDetailModel.auctionDetailStatusModel
            val chatStatus = auctionDetailModel.chatAuctionDetailModel

            return when {
                chatStatus.isChatParticipant && chatStatus.id != null -> EnterAuctionChatRoom
                chatStatus.isChatParticipant -> CreateAuctionChatRoom
                auctionStatus == AuctionDetailStatusModel.ONGOING || auctionStatus == AuctionDetailStatusModel.UNBIDDEN -> BidAuction
                else -> FinishAuction
            }
        }
    }
}
