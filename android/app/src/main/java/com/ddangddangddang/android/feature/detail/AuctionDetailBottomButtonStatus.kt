package com.ddangddangddang.android.feature.detail

import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.ChatAuctionDetailModel

enum class AuctionDetailBottomButtonStatus(
    @StringRes val text: Int,
    val enabled: Boolean,
) {
    BidAuction(R.string.detail_auction_submit, true),

    FinishAuction(R.string.detail_auction_finish, false),

    EnterAuctionChatRoom(R.string.detail_auction_chat_room_entrance, true),

    MyAuction(R.string.detail_auction_my_auction, false),

    AlreadyLastBidder(R.string.detail_auction_already_last_bidder, false),
    ;

    companion object {
        fun find(
            auctionDetailModel: AuctionDetailModel,
        ): AuctionDetailBottomButtonStatus {
            val isOwner = auctionDetailModel.isOwner
            val auctionStatus = auctionDetailModel.auctionDetailStatusModel
            val chatStatus = auctionDetailModel.chatAuctionDetailModel
            val isLastBidder = auctionDetailModel.isLastBidder

            return when {
                canEnterMessageRoom(chatStatus) -> EnterAuctionChatRoom
                canBidAuction(auctionStatus, isOwner) -> {
                    if (isLastBidder) {
                        AlreadyLastBidder
                    } else {
                        BidAuction
                    }
                }

                isOwner -> MyAuction
                else -> FinishAuction
            }
        }

        private fun canEnterMessageRoom(chatStatus: ChatAuctionDetailModel): Boolean {
            return chatStatus.isChatParticipant
        }

        private fun canBidAuction(
            auctionStatus: AuctionDetailStatusModel,
            isOwner: Boolean,
        ): Boolean {
            return (auctionStatus == AuctionDetailStatusModel.ONGOING || auctionStatus == AuctionDetailStatusModel.UNBIDDEN) && isOwner.not()
        }
    }
}
