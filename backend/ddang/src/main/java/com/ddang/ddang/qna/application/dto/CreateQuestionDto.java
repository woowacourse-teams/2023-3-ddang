package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.presentation.dto.request.CreateQuestionRequest;
import com.ddang.ddang.user.domain.User;

public record CreateQuestionDto(Long auctionId, String content, Long userId) {

    public static CreateQuestionDto of(final CreateQuestionRequest questionRequest, final Long userId) {
        return new CreateQuestionDto(questionRequest.auctionId(), questionRequest.content(), userId);
    }

    public Question toEntity(final Auction auction, final User questioner) {
        return new Question(auction, questioner, content);
    }
}
