package com.ddang.ddang.questionandanswer.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.questionandanswer.presentation.dto.CreateQuestionRequest;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionControllerFixture extends CommonControllerSliceTest {

    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 존재하지_않는_사용자_ID_클레임 = new PrivateClaims(999L);
    protected Long 생성된_질문_아이디 = 1L;
    protected String 액세스_토큰_값 = "Bearer accessToken";

    protected CreateQuestionRequest 질문_등록_request = new CreateQuestionRequest(1L, "궁금한 점이 있습니다.");
    protected CreateQuestionRequest 경매_아이디가_없는_질문_등록_request = new CreateQuestionRequest(null, "궁금한 점이 있습니다.");
    protected CreateQuestionRequest 경매_아이디가_음수인_질문_등록_request = new CreateQuestionRequest(-1L, "궁금한 점이 있습니다.");
    protected static CreateQuestionRequest 질문_내용이_null인_질문_등록_request = new CreateQuestionRequest(1L, null);
    protected static CreateQuestionRequest 질문_내용이_빈값인_질문_등록_request = new CreateQuestionRequest(1L, "");
    protected CreateQuestionRequest 존재하지_않는_경매에_대한_질문_등록_request = new CreateQuestionRequest(999L, "궁금한 점이 있습니다.");
    protected CreateQuestionRequest 종료된_경매에_대한_질문_등록_request = 질문_등록_request;
    protected CreateQuestionRequest 삭제된_경매에_대한_질문_등록_request = 질문_등록_request;
    protected CreateQuestionRequest 판매자가_본인_경매에_대한_질문_등록_request = 질문_등록_request;
}
