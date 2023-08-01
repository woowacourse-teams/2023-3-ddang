package com.ddang.ddang.bid.presentation;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.presentation.dto.CreateBidRequest;
import com.ddang.ddang.bid.presentation.dto.LoginUserRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<Void> create(
            @LoginUser final LoginUserRequest userRequest,
            @RequestBody @Valid final CreateBidRequest bidRequest
    ) {
        bidService.create(LoginUserDto.from(userRequest), CreateBidDto.from(bidRequest));

        return ResponseEntity.created(URI.create("/auctions/" + bidRequest.auctionId()))
                             .build();
    }
}
