package com.ddangddangddang.android.feature.detail

import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.ChatAuctionDetailModel

sealed class AuctionDetailBottomButtonStatus(
    @StringRes val text: Int,
    val enabled: Boolean,
) {
    object BidAuction : AuctionDetailBottomButtonStatus(R.string.detail_auction_submit, true)

    object FinishAuction :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_finish, false)

    object EnterAuctionChatRoom :
        AuctionDetailBottomButtonStatus(R.string.detail_auction_chat_room_entrance, true)

    companion object {
        fun find(
            auctionDetailModel: AuctionDetailModel,
        ): AuctionDetailBottomButtonStatus {
            val auctionStatus = auctionDetailModel.auctionDetailStatusModel
            val chatStatus = auctionDetailModel.chatAuctionDetailModel

            return when {
                canEnterMessageRoom(chatStatus) -> EnterAuctionChatRoom
                canBidAuction(auctionStatus) -> BidAuction
                else -> FinishAuction
            }
        }

        private fun canEnterMessageRoom(chatStatus: ChatAuctionDetailModel): Boolean {
            return chatStatus.isChatParticipant
        }

        private fun canBidAuction(auctionStatus: AuctionDetailStatusModel): Boolean {
            return (auctionStatus == AuctionDetailStatusModel.ONGOING || auctionStatus == AuctionDetailStatusModel.UNBIDDEN)
        }
    }
}
