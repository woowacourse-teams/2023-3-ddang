package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.auction.presentation.dto.response.CreateAuctionResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionDetailResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private static final String AUCTIONS_IMAGE_BASE_URL = "/auctions/images/";

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<CreateAuctionResponse> create(
            @RequestPart final List<MultipartFile> images,
            @RequestPart @Valid final CreateAuctionRequest request
    ) {
        final CreateInfoAuctionDto createInfoAuctionDto = auctionService.create(CreateAuctionDto.from(
                request,
                images,
                // TODO 3차 데모데이 이후 리펙토링 예정
                1L
        ));
        final CreateAuctionResponse response = CreateAuctionResponse.of(createInfoAuctionDto, calculateBaseImageUrl());

        return ResponseEntity.created(URI.create("/auctions/" + createInfoAuctionDto.id()))
                             .body(response);
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<ReadAuctionDetailResponse> read(@PathVariable final Long auctionId) {
        final ReadAuctionDto readAuctionDto = auctionService.readByAuctionId(auctionId);
        final ReadAuctionDetailResponse response = ReadAuctionDetailResponse.from(
                readAuctionDto,
                calculateBaseImageUrl()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ReadAuctionsResponse> readAllByLastAuctionId(
            @RequestParam(required = false) final Long lastAuctionId,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        final ReadAuctionsDto readAuctionsDto = auctionService.readAllByLastAuctionId(lastAuctionId, size);
        final ReadAuctionsResponse response = ReadAuctionsResponse.of(readAuctionsDto, calculateBaseImageUrl());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Void> delete(@PathVariable final Long auctionId) {
        // TODO 3차 데모데이 이후 리펙토링 예정
        auctionService.deleteByAuctionId(auctionId, 1L);

        return ResponseEntity.noContent()
                             .build();
    }

    private String calculateBaseImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .build()
                                          .toUriString()
                                          .concat(AUCTIONS_IMAGE_BASE_URL);
    }
}
