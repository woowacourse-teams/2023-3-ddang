package com.ddang.ddang.report.presentation;

import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.presentation.dto.request.LoginUserRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUser;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.presentation.dto.CreateAuctionReportRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
