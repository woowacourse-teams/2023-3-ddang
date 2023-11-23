package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.presentation.dto.response.ReadReviewDetailResponse;
import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.review.application.dto.response.ReadReviewDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{auctionId}/reviews")
    public ResponseEntity<ReadReviewDetailResponse> readByAuctionId(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long auctionId
    ) {
        final ReadReviewDetailDto readReviewDetailDto = reviewService.readByAuctionIdAndWriterId(userInfo.userId(), auctionId);
        ReadReviewDetailResponse response = ReadReviewDetailResponse.from(readReviewDetailDto);

        return ResponseEntity.ok(response);
    }
}
