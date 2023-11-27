package com.ddang.ddang.auction.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.AnswerDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.AnswerDto.AnswererInfoDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.QuestionDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.QuestionDto.QuestionerInfoDto;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionQuestionControllerFixture extends CommonControllerSliceTest {

    protected String 엑세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected Long 조회할_경매_아이디 = 1L;

    protected QuestionerInfoDto 질문자_정보_dto = new QuestionerInfoDto(
            1L,
            "질문자",
            "store-name.png",
            4.5d,
            "12345",
            false
    );
    protected AnswererInfoDto 판매자_정보_dto = new AnswererInfoDto(
            2L,
            "판매자",
            "store-name.png",
            4.5d,
            "12346",
            false
    );
    protected QuestionDto 질문_정보_dto1 = new QuestionDto(1L, 질문자_정보_dto, "질문1", LocalDateTime.now(), false, false);
    protected QuestionDto 질문_정보_dto2 = new QuestionDto(2L, 질문자_정보_dto, "질문2", LocalDateTime.now(), false, false);
    protected AnswerDto 답변_정보_dto1 = new AnswerDto(1L, 판매자_정보_dto, "답변1", LocalDateTime.now(), false);
    protected AnswerDto 답변_정보_dto2 = new AnswerDto(2L, 판매자_정보_dto, "답변1", LocalDateTime.now(), false);
    private QnaInfoDto 질문과_답변_정보_dto1 = new QnaInfoDto(질문_정보_dto1, 답변_정보_dto1);
    private QnaInfoDto 질문과_답변_정보_dto2 = new QnaInfoDto(질문_정보_dto2, 답변_정보_dto2);
    protected ReadMultipleQnaDto 질문과_답변_정보들_dto =
            new ReadMultipleQnaDto(List.of(질문과_답변_정보_dto1, 질문과_답변_정보_dto2));
}
