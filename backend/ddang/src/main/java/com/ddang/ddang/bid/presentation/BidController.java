package com.ddang.ddang.bid.presentation;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.CreateUserDto;
import com.ddang.ddang.bid.presentation.dto.CreateBidRequest;
import com.ddang.ddang.bid.presentation.dto.CreateUserRequest;
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
            @LoginUser final CreateUserRequest userRequest,
            @RequestBody @Valid final CreateBidRequest bidRequest
    ) {
        bidService.create(CreateUserDto.from(userRequest), CreateBidDto.from(bidRequest));

        return ResponseEntity.created(URI.create("/auctions/" + bidRequest.auctionId()))
                             .build();
    }
}
