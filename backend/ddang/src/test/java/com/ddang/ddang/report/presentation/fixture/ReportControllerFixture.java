package com.ddang.ddang.report.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.report.application.dto.CreateQuestionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionInReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomInReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadReporterDto;
import com.ddang.ddang.report.application.dto.ReadUserInReportDto;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateChatRoomReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateQuestionReportRequest;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class ReportControllerFixture extends CommonControllerSliceTest {

    protected Long 생성된_경매_신고_아이디 = 1L;
    protected Long 생성된_채팅방_신고_아이디 = 1L;
    protected Long 생성된_질문_신고_아이디 = 1L;
    protected String 엑세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 존재하지_않는_사용자_ID_클레임 = new PrivateClaims(-999L);
    protected PrivateClaims 채팅방_참여자가_아닌_사용자_ID_클레임 = new PrivateClaims(999L);
    protected CreateAuctionReportRequest 경매_신고_request = new CreateAuctionReportRequest(1L, "신고합니다");
    protected CreateAuctionReportRequest 경매_아이디가_없는_신고_request = new CreateAuctionReportRequest(null, "신고합니다");
    protected CreateAuctionReportRequest 경매_아이디가_음수인_신고_request = new CreateAuctionReportRequest(-999L, "신고합니다");
    protected static CreateAuctionReportRequest 신고_내용이_null인_경매_신고_request = new CreateAuctionReportRequest(1L, null);
    protected static CreateAuctionReportRequest 신고_내용이_빈값인_경매_신고_request = new CreateAuctionReportRequest(1L, "");
    private ReadUserInReportDto 판매자_정보_dto = new ReadUserInReportDto(1L, "판매자", 1L, 4.0d, "12345", false);
    private ReadAuctionInReportDto 신고할_경매_정보_dto = new ReadAuctionInReportDto(
            1L,
            판매자_정보_dto,
            "제목",
            "설명",
            100,
            1_00,
            false,
            LocalDateTime.now().plusDays(2),
            2
    );
    protected ReadAuctionReportDto 경매_신고_dto1 = new ReadAuctionReportDto(
            1L,
            new ReadReporterDto(2L, "회원1", 2L, 5.0, false),
            LocalDateTime.now(),
            신고할_경매_정보_dto,
            "신고합니다."
    );
    protected ReadAuctionReportDto 경매_신고_dto2 = new ReadAuctionReportDto(
            2L,
            new ReadReporterDto(3L, "회원2", 3L, 5.0, false),
            LocalDateTime.now(),
            신고할_경매_정보_dto,
            "신고합니다."
    );
    protected ReadAuctionReportDto 경매_신고_dto3 = new ReadAuctionReportDto(
            3L,
            new ReadReporterDto(4L, "회원3", 4L, 5.0, false),
            LocalDateTime.now(),
            신고할_경매_정보_dto,
            "신고합니다."
    );

    protected CreateChatRoomReportRequest 채팅방_신고_request = new CreateChatRoomReportRequest(1L, "신고합니다");
    protected CreateChatRoomReportRequest 존재하지_않는_채팅방_신고_request = new CreateChatRoomReportRequest(9999L, "신고합니다");
    protected CreateChatRoomReportRequest 채팅방_아이디가_null인_신고_request = new CreateChatRoomReportRequest(null, "신고합니다");
    protected CreateChatRoomReportRequest 채팅방_아이디가_음수인_신고_request = new CreateChatRoomReportRequest(-999L, "신고합니다");
    protected static CreateChatRoomReportRequest 신고_내용이_null인_채팅_신고_request = new CreateChatRoomReportRequest(1L, null);
    protected static CreateChatRoomReportRequest 신고_내용이_빈값인_채팅_신고_request = new CreateChatRoomReportRequest(-999L, "");
    private ReadAuctionInReportDto 신고할_채팅방의_경매_정보_dto1 = new ReadAuctionInReportDto(
            1L,
            판매자_정보_dto,
            "제목",
            "설명",
            100,
            1_00,
            false,
            LocalDateTime.now().plusDays(2),
            2
    );
    private ReadUserInReportDto 구매자_정보_dto1 = new ReadUserInReportDto(2L, "구매자1", 2L, 4.0d, "12346", false);
    private ReadReporterDto 신고자_정보_dto1 = new ReadReporterDto(2L, "구매자1", 2L, 4.0d, false);
    protected ReadChatRoomReportDto 채팅방_신고1 = new ReadChatRoomReportDto(
            1L,
            신고자_정보_dto1,
            LocalDateTime.now(),
            new ReadChatRoomInReportDto(1L, 신고할_채팅방의_경매_정보_dto1, 구매자_정보_dto1),
            "신고합니다."
    );
    private ReadAuctionInReportDto 신고할_채팅방의_경매_정보_dto2 = new ReadAuctionInReportDto(
            2L,
            판매자_정보_dto,
            "제목",
            "설명",
            100,
            1_00,
            false,
            LocalDateTime.now().plusDays(2),
            2
    );
    private ReadUserInReportDto 구매자_정보_dto2 = new ReadUserInReportDto(3L, "구매자2", 3L, 4.0d, "12347", false);
    private ReadReporterDto 신고자_정보_dto2 = new ReadReporterDto(3L, "구매자2", 3L, 4.0d, false);
    protected ReadChatRoomReportDto 채팅방_신고_dto2 = new ReadChatRoomReportDto(
            2L,
            신고자_정보_dto2,
            LocalDateTime.now(),
            new ReadChatRoomInReportDto(1L, 신고할_채팅방의_경매_정보_dto2, 구매자_정보_dto2),
            "신고합니다."
    );
    private ReadAuctionInReportDto 신고할_채팅방의_경매_정보_dto3 = new ReadAuctionInReportDto(
            3L,
            판매자_정보_dto,
            "제목",
            "설명",
            100,
            1_00,
            false,
            LocalDateTime.now().plusDays(2),
            2
    );
    private ReadUserInReportDto 구매자_정보_dto3 = new ReadUserInReportDto(3L, "구매자2", 3L, 4.0d, "12347", false);
    private ReadReporterDto 신고자_정보_dto3 = new ReadReporterDto(3L, "구매자2", 3L, 4.0d, false);
    protected ReadChatRoomReportDto 채팅방_신고_dto3 = new ReadChatRoomReportDto(
            3L,
            신고자_정보_dto3,
            LocalDateTime.now(),
            new ReadChatRoomInReportDto(1L, 신고할_채팅방의_경매_정보_dto3, 구매자_정보_dto3),
            "신고합니다."
    );

    protected CreateQuestionReportRequest 질문_신고_request = new CreateQuestionReportRequest(1L, 1L, "신고합니다.");
    protected CreateQuestionReportRequest 존재하지_않는_질문_신고_request = new CreateQuestionReportRequest(1L, 999L, "신고합니다.");
    protected CreateQuestionReportRequest 본인의_질문_신고_request = new CreateQuestionReportRequest(1L, 1L, "신고합니다.");
    protected CreateQuestionReportRequest 이미_신고한_질문_신고_request = new CreateQuestionReportRequest(1L, 1L, "신고합니다.");
    protected CreateQuestionReportRequest 경매_아이디가_null인_질문_신고_request = new CreateQuestionReportRequest(null, 1L, "신고합니다.");
    protected CreateQuestionReportRequest 경매_아이디가_음수인_질문_신고_request = new CreateQuestionReportRequest(-1L, 1L, "신고합니다.");
    protected CreateQuestionReportRequest 질문_아이디가_null인_질문_신고_request = new CreateQuestionReportRequest(1L, null, "신고합니다.");
    protected CreateQuestionReportRequest 질문_아이디가_음수인_질문_신고_request = new CreateQuestionReportRequest(1L, -1L, "신고합니다.");
    protected static CreateQuestionReportRequest 신고_내용이_null인_질문_신고_request = new CreateQuestionReportRequest(1L, 1L, null);
    protected static CreateQuestionReportRequest 신고_내용이_빈값인_질문_신고_request = new CreateQuestionReportRequest(1L, 1L, "");
}
