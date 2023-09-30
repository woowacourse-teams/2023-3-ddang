package com.ddang.ddang.chat.presentation.fixture;

import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.configuration.CommonControllerSliceTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomControllerFixture extends CommonControllerSliceTest {

    protected TokenDecoder tokenDecoder = mock(TokenDecoder.class);
    protected PrivateClaims 비공개_클레임 = new PrivateClaims(1L);
    protected ReadUserInChatRoomDto 판매자 = new ReadUserInChatRoomDto(1L, "사용자1", 1L, 5.0d, false);
    private ReadUserInChatRoomDto 구매자1 = new ReadUserInChatRoomDto(2L, "사용자2", 2L, 5.0d, false);
    private ReadUserInChatRoomDto 구매자2 = new ReadUserInChatRoomDto(3L, "사용자3", 3L, 5.0d, false);
    private ReadAuctionInChatRoomDto 조회용_경매1 = new ReadAuctionInChatRoomDto(1L, "경매1", 10_000, 1L);
    private ReadAuctionInChatRoomDto 조회용_경매2 = new ReadAuctionInChatRoomDto(2L, "경매2", 20_000, 1L);
    protected ReadChatRoomWithLastMessageDto 조회용_채팅방1 = new ReadChatRoomWithLastMessageDto(1L, 조회용_경매1, 구매자1, new ReadLastMessageDto(1L, LocalDateTime.now(), 판매자, 구매자1, "메시지1"), true);
    protected ReadChatRoomWithLastMessageDto 조회용_채팅방2 = new ReadChatRoomWithLastMessageDto(2L, 조회용_경매2, 구매자2, new ReadLastMessageDto(1L, LocalDateTime.now(), 판매자, 구매자2, "메시지2"), true);
    protected CreateMessageRequest 메시지_생성_요청 = new CreateMessageRequest(1L, "메시지 내용");
    protected CreateMessageRequest 유효하지_않은_발신자의_메시지_생성_요청 = new CreateMessageRequest(-999L, "메시지 내용");
    protected ReadMessageDto 조회용_메시지 = new ReadMessageDto(1L, LocalDateTime.now(), 1L, 1L, 1L, "메시지내용");
    protected ReadParticipatingChatRoomDto 조회용_참가중인_채팅방 = new ReadParticipatingChatRoomDto(1L, 조회용_경매1, 판매자, true);
    protected CreateChatRoomRequest 채팅방_생성_요청 = new CreateChatRoomRequest(1L);
    protected CreateChatRoomRequest 유효하지_않은_경매_아이디_채팅방_생성_요청 = new CreateChatRoomRequest(1L);
    protected final Long 유효하지_않은_사용자_아이디 = -999L;
    protected final Long 채팅방_아이디 = 1L;
    protected final Long 유효하지_않은_채팅방_아이디 = -999L;
    protected final Long 마지막_메시지_아이디 = 1L;
    protected final Long 유효하지_않은_마지막_메시지_아이디 = -999L;
}
