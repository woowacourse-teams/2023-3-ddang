package com.ddang.ddang.user.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.configuration.DescendingSort;
import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.user.presentation.dto.response.ReadAuctionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users/auctions")
@RequiredArgsConstructor
public class UserAuctionController {

    private static final String AUCTIONS_IMAGE_BASE_URL = "/auctions/images/";

    private final AuctionService auctionService;

    @GetMapping("/mine")
    public ResponseEntity<ReadAuctionsResponse> readAllByUserInfo(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @DescendingSort final Pageable pageable
    ) {
        final ReadAuctionsDto readAuctionsDto = auctionService.readAllByUserId(userInfo.userId(), pageable);
        final ReadAuctionsResponse response = ReadAuctionsResponse.of(readAuctionsDto, calculateBaseImageUrl());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bids")
    public ResponseEntity<ReadAuctionsResponse> readAllByBids(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @DescendingSort final Pageable pageable
    ) {
        final ReadAuctionsDto readAuctionsDto = auctionService.readAllByBidderId(userInfo.userId(), pageable);
        final ReadAuctionsResponse response = ReadAuctionsResponse.of(readAuctionsDto, calculateBaseImageUrl());

        return ResponseEntity.ok(response);
    }

    private String calculateBaseImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                                          .concat(AUCTIONS_IMAGE_BASE_URL);
    }
}
