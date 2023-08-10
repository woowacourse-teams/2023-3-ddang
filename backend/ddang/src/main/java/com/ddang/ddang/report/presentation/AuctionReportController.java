package com.ddang.ddang.report.presentation;

import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.presentation.dto.request.LoginUserRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUser;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
import com.ddang.ddang.report.presentation.dto.response.ReadAuctionReportsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class AuctionReportController {

    private final AuctionReportService auctionReportService;

    @PostMapping("/auctions")
    public ResponseEntity<Void> create(
            @LoginUser final LoginUserRequest userRequest,
            @RequestBody @Valid final CreateAuctionReportRequest auctionReportRequest
    ) {
        auctionReportService.create(LoginUserDto.from(userRequest), CreateAuctionReportDto.from(auctionReportRequest));

        return ResponseEntity.created(URI.create("/auctions/" + auctionReportRequest.auctionId()))
                             .build();
    }

    @GetMapping("/auctions")
    public ResponseEntity<ReadAuctionReportsResponse> readAll() {
        final List<ReadAuctionReportDto> readAuctionReportDtos = auctionReportService.readAll();
        final ReadAuctionReportsResponse response = ReadAuctionReportsResponse.from(readAuctionReportDtos);

        return ResponseEntity.ok(response);
    }
}
