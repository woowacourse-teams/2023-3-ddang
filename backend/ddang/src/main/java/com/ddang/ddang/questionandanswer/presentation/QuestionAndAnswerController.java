package com.ddang.ddang.questionandanswer.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.questionandanswer.application.QuestionService;
import com.ddang.ddang.questionandanswer.application.dto.CreateQuestionDto;
import com.ddang.ddang.questionandanswer.presentation.dto.CreateQuestionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticateUser AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateQuestionRequest questionRequest
    ) {
        questionService.create(CreateQuestionDto.of(questionRequest, userInfo.userId()));

        return ResponseEntity.created(URI.create("/auctions/" + questionRequest.auctionId()))
                             .build();
    }
}
