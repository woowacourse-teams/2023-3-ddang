package com.ddang.ddang.report.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.report.application.AnswerReportService;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.ChatRoomReportService;
import com.ddang.ddang.report.application.QuestionReportService;
import com.ddang.ddang.report.application.dto.request.CreateAnswerReportDto;
import com.ddang.ddang.report.application.dto.request.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.request.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.dto.request.CreateQuestionReportDto;
import com.ddang.ddang.report.application.dto.response.ReadAnswerReportDto;
import com.ddang.ddang.report.application.dto.response.ReadAuctionReportDto;
import com.ddang.ddang.report.application.dto.response.ReadChatRoomReportDto;
import com.ddang.ddang.report.application.dto.response.ReadQuestionReportDto;
import com.ddang.ddang.report.presentation.dto.request.CreateAnswerReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateChatRoomReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateQuestionReportRequest;
import com.ddang.ddang.report.presentation.dto.response.ReadMultipleAnswerReportResponse;
import com.ddang.ddang.report.presentation.dto.response.ReadMultipleAuctionReportResponse;
import com.ddang.ddang.report.presentation.dto.response.ReadMultipleChatRoomReportResponse;
import com.ddang.ddang.report.presentation.dto.response.ReadMultipleQuestionReportResponse;
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
    private final QuestionReportService questionReportService;
    private final AnswerReportService answerReportService;

    @PostMapping("/auctions")
    public ResponseEntity<Void> createAuctionReport(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateAuctionReportRequest auctionReportRequest
    ) {
        auctionReportService.create(CreateAuctionReportDto.of(auctionReportRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + auctionReportRequest.auctionId()))
                             .build();
    }

    @GetMapping("/auctions")
    public ResponseEntity<ReadMultipleAuctionReportResponse> readAllAuctionReport() {
        final List<ReadAuctionReportDto> readAuctionReportDtos = auctionReportService.readAll();
        final ReadMultipleAuctionReportResponse response = ReadMultipleAuctionReportResponse.from(readAuctionReportDtos);

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
    public ResponseEntity<ReadMultipleChatRoomReportResponse> readAllChatRoomReport() {
        final List<ReadChatRoomReportDto> readAuctionReportDtos = chatRoomReportService.readAll();
        final ReadMultipleChatRoomReportResponse response = ReadMultipleChatRoomReportResponse.from(readAuctionReportDtos);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/questions")
    public ResponseEntity<Void> createQuestionReport(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateQuestionReportRequest createQuestionReportRequest
    ) {
        questionReportService.create(CreateQuestionReportDto.of(createQuestionReportRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + createQuestionReportRequest.auctionId() + "/questions"))
                             .build();
    }

    @GetMapping("/questions")
    public ResponseEntity<ReadMultipleQuestionReportResponse> readAllQuestionReport() {
        final List<ReadQuestionReportDto> readQuestionReportDtos = questionReportService.readAll();
        final ReadMultipleQuestionReportResponse response = ReadMultipleQuestionReportResponse.from(readQuestionReportDtos);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/answers")
    public ResponseEntity<Void> createAnswerReport(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateAnswerReportRequest createAnswerReportRequest
    ) {
        answerReportService.create(CreateAnswerReportDto.of(createAnswerReportRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + createAnswerReportRequest.auctionId() + "/questions"))
                             .build();
    }

    @GetMapping("/answers")
    public ResponseEntity<ReadMultipleAnswerReportResponse> readAllAnswerReport() {
        final List<ReadAnswerReportDto> readAnswerReportDtos = answerReportService.readAll();
        final ReadMultipleAnswerReportResponse response = ReadMultipleAnswerReportResponse.from(readAnswerReportDtos);

        return ResponseEntity.ok(response);
    }
}
