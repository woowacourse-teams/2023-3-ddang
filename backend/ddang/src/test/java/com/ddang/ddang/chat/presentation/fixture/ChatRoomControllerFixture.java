package com.ddang.ddang.chat.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadChatRoomAuctionInfoDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadLastMessageDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadPartnerInfoDto;
import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto.ReadSimpleAuctionInfoDto;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.configuration.CommonControllerSliceTest;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomControllerFixture extends CommonControllerSliceTest {

    private Long 탈퇴한_사용자_아이디 = 5L;
    private ReadChatRoomAuctionInfoDto 조회용_경매1 = new ReadChatRoomAuctionInfoDto(1L, "경매1", 10_000, "store-name.png");
    private ReadChatRoomAuctionInfoDto 조회용_경매2 = new ReadChatRoomAuctionInfoDto(2L, "경매2", 20_000, "store-name.png");

    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected ReadMultipleChatRoomDto 조회용_채팅방1 = new ReadMultipleChatRoomDto(
            1L,
            조회용_경매1,
            new ReadPartnerInfoDto(2L, "구매자1", "store-name.png", 5.0d, false),
            new ReadLastMessageDto(LocalDateTime.now(), "메시지1"),
            1L,
            true);
    protected ReadMultipleChatRoomDto 조회용_채팅방2 = new ReadMultipleChatRoomDto(
            2L,
            조회용_경매2,
            new ReadPartnerInfoDto(3L, "구매자2", "store-name.png", 5.0d, false),
            new ReadLastMessageDto(LocalDateTime.now(), "메시지2"),
            1L,
            true);
    protected CreateMessageRequest 메시지_생성_요청 = new CreateMessageRequest(1L, "메시지 내용");
    protected CreateMessageRequest 유효하지_않은_발신자의_메시지_생성_요청 = new CreateMessageRequest(-999L, "메시지 내용");
    protected CreateMessageRequest 탈퇴한_사용자와의_메시지_생성_요청 = new CreateMessageRequest(탈퇴한_사용자_아이디, "메시지 내용");
    protected ReadMessageDto 조회용_메시지 = new ReadMessageDto(1L, LocalDateTime.now(), 1L, 1L, 1L, "메시지내용");
    protected ReadSingleChatRoomDto 조회용_참가중인_채팅방 = new ReadSingleChatRoomDto(
            1L,
            new ReadSimpleAuctionInfoDto(1L, "경매1", 10_000, "store-name.png"),
            new ReadSingleChatRoomDto.ReadPartnerInfoDto(1L, "판매자", "store-name.png", 5.0d, false),
            true);
    protected CreateChatRoomRequest 채팅방_생성_요청 = new CreateChatRoomRequest(1L);
    protected CreateChatRoomRequest 존재하지_않은_경매_아이디_채팅방_생성_요청 = new CreateChatRoomRequest(1L);
    protected CreateChatRoomRequest 유효하지_않은_경매_아이디_채팅방_생성_요청 = new CreateChatRoomRequest(-999L);
    protected Long 채팅방_아이디 = 1L;
    protected Long 유효하지_않은_채팅방_아이디 = -999L;
    protected Long 마지막_메시지_아이디 = 1L;
    protected Long 유효하지_않은_마지막_메시지_아이디 = -999L;
}
