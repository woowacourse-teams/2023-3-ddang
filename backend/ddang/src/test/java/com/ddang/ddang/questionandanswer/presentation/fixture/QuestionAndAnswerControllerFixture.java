package com.ddang.ddang.questionandanswer.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateAnswerRequest;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateQuestionRequest;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionAndAnswerControllerFixture extends CommonControllerSliceTest {

    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 존재하지_않는_사용자_ID_클레임 = new PrivateClaims(999L);
    protected Long 생성된_질문_아이디 = 1L;
    protected Long 생성된_답변_아이디 = 1L;
    protected Long 질문_아이디 = 1L;
    protected Long 존재하지_않는_질문_아이디 = 999L;
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

    protected CreateAnswerRequest 답변_등록_request = new CreateAnswerRequest(1L, "답변 드립니다.");
    protected CreateAnswerRequest 경매_아이디가_없는_답변_등록_request = new CreateAnswerRequest(null, "답변 드립니다.");
    protected CreateAnswerRequest 양수가_아닌_경매_아이디에_대한_답변_등록_request = new CreateAnswerRequest(-1L, "답변 드립니다.");
    protected static CreateAnswerRequest 답변_내용이_null인_답변_등록_request = new CreateAnswerRequest(-1L, null);
    protected static CreateAnswerRequest 답변_내용이_빈값인_답변_등록_request = new CreateAnswerRequest(-1L, "null");
    protected CreateAnswerRequest 판매자가_아닌_사용자가_질문에_대한_답변_등록_request = new CreateAnswerRequest(1L, "답변 드립니다.");
    protected CreateAnswerRequest 이미_답변한_질문에_대한_답변_등록_request = new CreateAnswerRequest(1L, "답변 드립니다.");
}
