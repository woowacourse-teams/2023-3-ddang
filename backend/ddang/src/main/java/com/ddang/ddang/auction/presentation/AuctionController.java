package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.presentation.dto.CreateAuctionRequest;
import com.ddang.ddang.auction.presentation.dto.CreateAuctionResponse;
import com.ddang.ddang.auction.presentation.dto.ReadAuctionDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<CreateAuctionResponse> create(@RequestBody @Valid final CreateAuctionRequest request) {
        final Long auctionId = auctionService.create(CreateAuctionDto.from(request));

        return ResponseEntity.created(URI.create("/auctions/" + auctionId))
                             .body(new CreateAuctionResponse(auctionId));
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<ReadAuctionDetailResponse> read(@PathVariable final Long auctionId) {
        final ReadAuctionDto readAuctionDto = auctionService.readByAuctionId(auctionId);

        return ResponseEntity.ok(ReadAuctionDetailResponse.from(readAuctionDto));
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Void> delete(@PathVariable final Long auctionId) {
        auctionService.deleteByAuctionId(auctionId);

        return ResponseEntity.noContent()
                             .build();
    }
}
