package com.ddang.ddang.bid.presentation;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.presentation.dto.CreateBidRequest;
import com.ddang.ddang.bid.presentation.dto.LoginUserRequest;
import com.ddang.ddang.bid.presentation.dto.ReadBidResponse;
import com.ddang.ddang.bid.presentation.dto.ReadBidsResponse;
import com.ddang.ddang.bid.presentation.resolver.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/{auctionId}")
    private ResponseEntity<ReadBidsResponse> readAllByAuctionId(@PathVariable final Long auctionId) {
        final List<ReadBidDto> readBidDtos = bidService.readAllByAuctionId(auctionId);
        final List<ReadBidResponse> readBidResponses = readBidDtos.stream()
                                                                  .map(ReadBidResponse::from)
                                                                  .toList();

        final ReadBidsResponse response = new ReadBidsResponse(readBidResponses);

        return ResponseEntity.ok(response);
    }
}
