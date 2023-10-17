package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.InvalidUserToChat;
import com.ddang.ddang.chat.application.fixture.ChatRoomServiceFixture;
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomServiceTest extends ChatRoomServiceFixture {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    MessageService messageService;

    @Autowired
    ReadMessageLogRepository readMessageLogRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ApplicationEvents events;

    @Test
    void 채팅방_생성시_채팅_참여자에_대한_로그를_생성한다() {
        // given
        final Long createdChatRoomId = chatRoomService.create(채팅방을_생성하는_메리.getId(), 메리가_생성하려는_채팅방);

        // when
        final Optional<ReadMessageLog> actual_메리 = readMessageLogRepository.findBy(채팅방을_생성하는_메리.getId(), createdChatRoomId);
        final Optional<ReadMessageLog> actual_지토 = readMessageLogRepository.findBy(메리_경매_낙찰자_지토.getId(), createdChatRoomId);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual_메리).isPresent();
            softAssertions.assertThat(actual_지토).isPresent();
        });
    }

    @Test
    void 채팅방을_생성한다() {
        // when
        final Long actual = chatRoomService.create(구매자.getId(), 채팅방_생성을_위한_DTO);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 채팅방_생성시_메시지_로그_생성_이벤트가_호출된다() {
        // when
        chatRoomService.create(구매자.getId(), 채팅방_생성을_위한_DTO);
        final long actual = events.stream(CreateReadMessageLogEvent.class).count();

        // then
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 채팅방_생성시_요청한_사용자_정보를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.create(존재하지_않는_사용자_아이디, 채팅방_생성을_위한_DTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    void 채팅방_생성시_관련된_경매_정보를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.create(구매자.getId(), 경매_정보가_없어서_채팅방을_생성할_수_없는_DTO))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 경매가_종료되지_않은_상태에서_채팅방을_생성하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.create(판매자.getId(), 경매가_진행중이라서_채팅방을_생성할_수_없는_DTO))
                .isInstanceOf(InvalidAuctionToChatException.class)
                .hasMessage("경매가 아직 종료되지 않았습니다.");
    }

    @Test
    void 낙찰자가_없는데_채팅방을_생성하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.create(판매자.getId(), 낙찰자가_없어서_채팅방을_생성할_수_없는_DTO))
                .isInstanceOf(WinnerNotFoundException.class)
                .hasMessage("낙찰자가 존재하지 않습니다");
    }

    @Test
    void 채팅방_생성을_요청한_사용자가_경매의_판매자_또는_최종_낙찰자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.create(경매에_참여한_적_없는_사용자.getId(), 채팅방_생성을_위한_DTO))
                .isInstanceOf(InvalidUserToChat.class)
                .hasMessage("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");
    }

    @Test
    void 해당_경매에_대한_채팅이_이미_존재할_경우_존재하는_채팅방의_아이디를_반환한다() {
        // when
        final Long actual = chatRoomService.create(엔초.getId(), 엔초_지토_채팅방_생성을_위한_DTO);

        // then
        assertThat(actual).isEqualTo(엔초_지토_채팅방.getId());
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_마지막에_전송된_메시지와_함께_조회하며_마지막_메시지가_최근인_순서로_정렬하여_조회한다() {
        // when
        final List<ReadChatRoomWithLastMessageDto> actual = chatRoomService.readAllByUserId(엔초.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(엔초_채팅_목록의_제이미_엔초_채팅방_정보.id());
            softAssertions.assertThat(actual.get(0).auctionDto()).isEqualTo(엔초_채팅_목록의_제이미_엔초_채팅방_정보.auctionDto());
            softAssertions.assertThat(actual.get(0).partnerDto()).isEqualTo(엔초_채팅_목록의_제이미_엔초_채팅방_정보.partnerDto());
            softAssertions.assertThat(actual.get(0).lastMessageDto())
                          .isEqualTo(엔초_채팅_목록의_제이미_엔초_채팅방_정보.lastMessageDto());
            softAssertions.assertThat(actual.get(0).isChatAvailable())
                          .isEqualTo(엔초_채팅_목록의_제이미_엔초_채팅방_정보.isChatAvailable());

            softAssertions.assertThat(actual.get(1).id()).isEqualTo(엔초_채팅_목록의_엔초_지토_채팅방_정보.id());
            softAssertions.assertThat(actual.get(1).auctionDto()).isEqualTo(엔초_채팅_목록의_엔초_지토_채팅방_정보.auctionDto());
            softAssertions.assertThat(actual.get(1).partnerDto()).isEqualTo(엔초_채팅_목록의_엔초_지토_채팅방_정보.partnerDto());
            softAssertions.assertThat(actual.get(1).lastMessageDto())
                          .isEqualTo(엔초_채팅_목록의_엔초_지토_채팅방_정보.lastMessageDto());
            softAssertions.assertThat(actual.get(1).isChatAvailable())
                          .isEqualTo(엔초_채팅_목록의_엔초_지토_채팅방_정보.isChatAvailable());
        });
    }

    @Test
    void 사용자가_참여한_채팅방_목록_조회시_사용자_정보를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.readAllByUserId(존재하지_않는_사용자_아이디))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_조회한다() {
        // when
        final ReadParticipatingChatRoomDto actual = chatRoomService.readByChatRoomId(엔초_지토_채팅방.getId(), 엔초.getId());

        // then
        assertThat(actual).isEqualTo(엔초가_조회한_엔초_지토_채팅방_정보_조회_결과);
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_조회를_요청한_사용자의_정보를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(엔초_지토_채팅방.getId(), 존재하지_않는_사용자_아이디))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자 정보를 찾을 수 없습니다.");

    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(존재하지_않는_채팅방_아이디, 엔초.getId()))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");

    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_주어진_사용자가_채팅의_참여자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(엔초_지토_채팅방.getId(), 제이미.getId()))
                .isInstanceOf(InvalidUserToChat.class)
                .hasMessageContaining("해당 채팅방에 접근할 권한이 없습니다.");
    }

    @Test
    void 지정한_경매_아이디와_관련된_채팅방_정보를_조회할_때_조회한_사람이_해당_채팅방_참여자라면_채팅방_아이디와_참여가능여부_참을_반환한다() {
        // when
        final ReadChatRoomDto actual = chatRoomService.readChatInfoByAuctionId(판매자_엔초_구매자_지토_경매.getId(), 엔초_회원_정보);

        // then
        assertThat(actual).isEqualTo(엔초_지토_채팅방_정보_및_참여_가능);
    }

    @Test
    void 지정한_경매_아이디와_관련된_채팅방_정보를_조회할_때_조회한_사람이_해당_채팅방_참여자가_아니라면_채팅방_아이디와_참여가능여부_거짓을_반환한다() {
        // when
        final ReadChatRoomDto actual =
                chatRoomService.readChatInfoByAuctionId(판매자_엔초_구매자_지토_경매.getId(), 경매에_참여한_적_없는_사용자_정보);

        // then
        assertThat(actual).isEqualTo(엔초_지토_채팅방_정보_및_참여_불가능);
    }

    @Test
    void 지정한_경매_아이디와_관련된_채팅방을_조회할_때_조회를_요청한_사용자_정보를_찾을_수_없다면_무조건_채팅방_아이디_null과_참여가능여부_거짓을_반환한다() {
        // when
        final ReadChatRoomDto actual = chatRoomService.readChatInfoByAuctionId(판매자_엔초_구매자_지토_경매.getId(), 존재하지_않는_사용자_정보);

        // then
        assertThat(actual).isEqualTo(채팅방_없고_참여_불가능);
    }

    @Test
    void 지정한_경매_아이디와_관련된_채팅방을_조회할_때_경매를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> chatRoomService.readChatInfoByAuctionId(존재하지_않는_경매_아이디, 엔초_회원_정보))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("지정한 아이디에 대한 경매를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_경매_아이디와_관련된_채팅방을_조회할_때_채팅방을_찾을_수_없다면_채팅방_아이디_null과_참여가능을_반환한다() {
        // when
        final ReadChatRoomDto actual = chatRoomService.readChatInfoByAuctionId(채팅방이_없는_경매.getId(), 판매자_회원_정보);

        // then
        assertThat(actual).isEqualTo(채팅방은_아직_없지만_참여_가능);
    }
}
