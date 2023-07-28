package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.auction.presentation.dto.response.CreateAuctionResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionDetailResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<CreateAuctionResponse> create(@ModelAttribute @Valid final CreateAuctionRequest request) {
        final Long auctionId = auctionService.create(CreateAuctionDto.from(request));
        final CreateAuctionResponse response = new CreateAuctionResponse(auctionId);

        return ResponseEntity.created(URI.create("/auctions/" + auctionId))
                             .body(response);
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<ReadAuctionDetailResponse> read(@PathVariable final Long auctionId) {
        final ReadAuctionDto readAuctionDto = auctionService.readByAuctionId(auctionId);
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .build()
                                                          .toUriString()
                                                          .concat("/auctions/images/");
        final ReadAuctionDetailResponse response = ReadAuctionDetailResponse.from(readAuctionDto, baseUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ReadAuctionsResponse> readAllByLastAuctionId(
            @RequestParam(required = false) final Long lastAuctionId,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        final List<ReadAuctionDto> readAuctionDtos = auctionService.readAllByLastAuctionId(lastAuctionId, size);
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .build()
                                                          .toUriString()
                                                          .concat("/auctions/images/");
        final List<ReadAuctionResponse> readAuctionResponses = readAuctionDtos.stream()
                                                                              .map(dto -> ReadAuctionResponse.of(
                                                                                      dto, baseUrl
                                                                              ))
                                                                              .toList();
        final ReadAuctionsResponse response = ReadAuctionsResponse.of(readAuctionResponses, size);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Void> delete(@PathVariable final Long auctionId) {
        auctionService.deleteByAuctionId(auctionId);

        return ResponseEntity.noContent()
                             .build();
    }
}
