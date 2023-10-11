package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;

public record QuestionDto(Auction auction, User writer, String content, Answer answer, boolean deleted, String auctionImageAbsoluteUrl){

    public static QuestionDto from(final Question question, String auctionImageAbsoluteUrl) {
        return new QuestionDto(
                question.getAuction(),
                question.getWriter(),
                question.getContent(),
                question.getAnswer(),
                question.isDeleted(),
                auctionImageAbsoluteUrl
        );
    }
}
