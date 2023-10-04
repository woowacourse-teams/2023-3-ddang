package com.ddang.ddang.auction.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.questionandanswer.application.dto.ReadAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswersDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadUserInQuestionAndAnswerDto;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionQuestionControllerFixture extends CommonControllerSliceTest {

    protected String 엑세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected Long 조회할_경매_아이디 = 1L;

    protected ReadUserInQuestionAndAnswerDto 질문자_정보_dto = new ReadUserInQuestionAndAnswerDto(
            1L,
            "질문자",
            1L,
            4.5d,
            "12345",
            false
    );
    protected ReadUserInQuestionAndAnswerDto 판매자_정보_dto = new ReadUserInQuestionAndAnswerDto(
            2L,
            "판매자",
            2L,
            4.5d,
            "12346",
            false
    );
    protected ReadQuestionDto 질문_정보_dto1 = new ReadQuestionDto(1L, 질문자_정보_dto, "질문1", LocalDateTime.now());
    protected ReadQuestionDto 질문_정보_dto2 = new ReadQuestionDto(2L, 질문자_정보_dto, "질문2", LocalDateTime.now());
    protected ReadAnswerDto 답변_정보_dto1 = new ReadAnswerDto(1L, 판매자_정보_dto, "답변1", LocalDateTime.now());
    protected ReadAnswerDto 답변_정보_dto2 = new ReadAnswerDto(2L, 판매자_정보_dto, "답변1", LocalDateTime.now());
    private ReadQuestionAndAnswerDto 질문과_답변_정보_dto1 = new ReadQuestionAndAnswerDto(질문_정보_dto1, 답변_정보_dto1);
    private ReadQuestionAndAnswerDto 질문과_답변_정보_dto2 = new ReadQuestionAndAnswerDto(질문_정보_dto2, 답변_정보_dto2);
    protected ReadQuestionAndAnswersDto 질문과_답변_정보들_dto =
            new ReadQuestionAndAnswersDto(List.of(질문과_답변_정보_dto1, 질문과_답변_정보_dto2));
}
