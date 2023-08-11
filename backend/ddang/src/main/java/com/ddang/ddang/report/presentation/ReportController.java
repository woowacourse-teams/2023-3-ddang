package com.ddang.ddang.report.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.ChatRoomReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateChatRoomReportRequest;
import com.ddang.ddang.report.presentation.dto.response.ReadAuctionReportsResponse;
import com.ddang.ddang.report.presentation.dto.response.ReadChatRoomReportsResponse;
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
public class ReportController {

    private final AuctionReportService auctionReportService;
    private final ChatRoomReportService chatRoomReportService;

    @PostMapping("/auctions")
    public ResponseEntity<Void> createAuctinReport(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateAuctionReportRequest auctionReportRequest
    ) {
        auctionReportService.create(CreateAuctionReportDto.of(auctionReportRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + auctionReportRequest.auctionId()))
                             .build();
    }

    @GetMapping("/auctions")
    public ResponseEntity<ReadAuctionReportsResponse> readAllAuctionReport() {
        final List<ReadAuctionReportDto> readAuctionReportDtos = auctionReportService.readAll();
        final ReadAuctionReportsResponse response = ReadAuctionReportsResponse.from(readAuctionReportDtos);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat-rooms")
    public ResponseEntity<Void> createChatRoomReport(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateChatRoomReportRequest createChatRoomReportRequest
    ) {
        chatRoomReportService.create(CreateChatRoomReportDto.of(createChatRoomReportRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/chattings/" + createChatRoomReportRequest.chatRoomId()))
                             .build();
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity<ReadChatRoomReportsResponse> readAllChatRoomReport() {
        final List<ReadChatRoomReportDto> readAuctionReportDtos = chatRoomReportService.readAll();
        final ReadChatRoomReportsResponse response = ReadChatRoomReportsResponse.from(readAuctionReportDtos);

        return ResponseEntity.ok(response);
    }
}