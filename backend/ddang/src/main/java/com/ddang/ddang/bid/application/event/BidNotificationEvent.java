package com.ddang.ddang.bid.application.event;

import com.ddang.ddang.bid.application.dto.BidDto;

public record BidNotificationEvent(BidDto bidDto) {
}
