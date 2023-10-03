package com.ddang.ddang.questionandanswer.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.questionandanswer.application.AnswerService;
import com.ddang.ddang.questionandanswer.application.QuestionService;
import com.ddang.ddang.questionandanswer.application.dto.CreateAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.CreateQuestionDto;
import com.ddang.ddang.questionandanswer.presentation.dto.CreateAnswerRequest;
import com.ddang.ddang.questionandanswer.presentation.dto.CreateQuestionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionAndAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<Void> createQuestion(
            @AuthenticateUser AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateQuestionRequest questionRequest
    ) {
        questionService.create(CreateQuestionDto.of(questionRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + questionRequest.auctionId()))
                             .build();
    }

    // TODO: 2023/08/18 [고민] auctionId를 클라이언트에게서 받기 vs create 후 생성된 객체 아이디가 아닌 경매 아이디도 받아오기 (일단 전자)
    @PostMapping("/{questionId}/answers")
    public ResponseEntity<Void> createAnswer(
            @AuthenticateUser AuthenticationUserInfo userInfo,
            @PathVariable final Long questionId,
            @RequestBody @Valid final CreateAnswerRequest answerRequest
    ) {
        answerService.create(CreateAnswerDto.of(questionId, answerRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + answerRequest.auctionId()))
                             .build();
    }
}
