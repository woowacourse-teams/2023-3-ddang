package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.configuration.DescendingSort;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.auction.presentation.dto.response.CreateAuctionResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionDetailResponse;
import com.ddang.ddang.auction.presentation.dto.response.ReadAuctionsResponse;
import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<CreateAuctionResponse> create(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestPart final List<MultipartFile> images,
            @RequestPart @Valid final CreateAuctionRequest request
    ) {
        final CreateInfoAuctionDto createInfoAuctionDto = auctionService.create(CreateAuctionDto.of(
                request,
                images,
                userInfo.userId()
        ));
        final CreateAuctionResponse response = CreateAuctionResponse.from(createInfoAuctionDto);

        return ResponseEntity.created(URI.create("/auctions/" + createInfoAuctionDto.id()))
                             .body(response);
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<ReadAuctionDetailResponse> read(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long auctionId
    ) {
        final ReadAuctionWithChatRoomIdDto readAuctionDto = auctionService.readByAuctionId(auctionId, userInfo);
        final ReadAuctionDetailResponse response = ReadAuctionDetailResponse.of(
                readAuctionDto,
                userInfo
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ReadAuctionsResponse> readAllByLastAuctionId(
            @AuthenticateUser final AuthenticationUserInfo ignored,
            @RequestParam(required = false) final Long lastAuctionId,
            @DescendingSort final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition
    ) {
        final ReadAuctionsDto readAuctionsDto = auctionService.readAllByLastAuctionId(
                lastAuctionId,
                pageable,
                readAuctionSearchCondition
        );
        final ReadAuctionsResponse response = ReadAuctionsResponse.from(readAuctionsDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Void> delete(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long auctionId
    ) {
        auctionService.deleteByAuctionId(auctionId, userInfo.userId());

        return ResponseEntity.noContent().build();
    }
}
