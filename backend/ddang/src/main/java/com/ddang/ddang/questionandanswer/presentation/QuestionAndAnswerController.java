package com.ddang.ddang.questionandanswer.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.questionandanswer.application.AnswerService;
import com.ddang.ddang.questionandanswer.application.QuestionService;
import com.ddang.ddang.questionandanswer.application.dto.CreateAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.CreateQuestionDto;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateAnswerRequest;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateQuestionRequest;
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
