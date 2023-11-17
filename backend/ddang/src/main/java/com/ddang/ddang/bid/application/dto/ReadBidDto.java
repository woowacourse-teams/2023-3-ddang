package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.image.application.util.ImageStoreNameProcessor;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadBidDto(
        String name,
        String profileImageStoreName,
        boolean isDeletedUser,
        int price,
        LocalDateTime bidTime
) {

    public static ReadBidDto from(final Bid bid) {
        final User bidder = bid.getBidder();

        return new ReadBidDto(
                bidder.getName(),
                ImageStoreNameProcessor.process(bidder.getProfileImage()),
                bidder.isDeleted(),
                bid.getPrice().getValue(),
                bid.getCreatedTime()
        );
    }
}
