package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.presentation.dto.response.ReadQuestionAndAnswersResponse;
import com.ddang.ddang.questionandanswer.application.QuestionService;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionQuestionController {

    private final QuestionService questionService;

    @GetMapping("/{auctionId}/questions")
    public ResponseEntity<ReadQuestionAndAnswersResponse> readAllByAuctionId(@PathVariable final Long auctionId) {
        final ReadQuestionAndAnswersDto readQuestionAndAnswersDto = questionService.readAllByAuctionId(auctionId);
        final ReadQuestionAndAnswersResponse response = ReadQuestionAndAnswersResponse.from(readQuestionAndAnswersDto);

        return ResponseEntity.ok(response);
    }
}
