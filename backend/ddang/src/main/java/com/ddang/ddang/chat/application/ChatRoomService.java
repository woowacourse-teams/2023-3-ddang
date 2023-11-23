package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.chat.application.dto.request.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto;
import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.InvalidUserToChat;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.chat.domain.repository.MultipleChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private static final Long DEFAULT_CHAT_ROOM_ID = null;

    private final ApplicationEventPublisher messageLogEventPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final MultipleChatRoomRepository multipleChatRoomRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @Transactional
    public Long create(final Long userId, final CreateChatRoomDto chatRoomDto) {
        final User findUser = userRepository.getByIdOrThrow(userId);
        final Auction findAuction = auctionRepository.getTotalAuctionByIdOrThrow(chatRoomDto.auctionId());

        return chatRoomRepository.findChatRoomIdByAuctionId(findAuction.getId())
                                 .orElseGet(() -> createChatRoom(findUser, findAuction));
    }

    private Long createChatRoom(final User findUser, final Auction findAuction) {
        final ChatRoom persistChatRoom = persistChatRoom(findUser, findAuction);

        messageLogEventPublisher.publishEvent(new CreateReadMessageLogEvent(persistChatRoom));

        return persistChatRoom.getId();
    }

    private ChatRoom persistChatRoom(final User user, final Auction auction) {
        checkAuctionStatus(auction);

        final User winner = auction.findWinner(LocalDateTime.now())
                                   .orElseThrow(() -> new WinnerNotFoundException("낙찰자가 존재하지 않습니다"));

        checkUserCanParticipate(user, auction);

        final ChatRoom chatRoom = new ChatRoom(auction, winner);

        return chatRoomRepository.save(chatRoom);
    }

    private void checkAuctionStatus(final Auction findAuction) {
        if (!findAuction.isClosed(LocalDateTime.now())) {
            throw new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다.");
        }
    }

    private void checkUserCanParticipate(final User findUser, final Auction findAuction) {
        if (isNotSellerAndNotWinner(findUser, findAuction)) {
            throw new InvalidUserToChat("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");
        }
    }

    private boolean isNotSellerAndNotWinner(final User findUser, final Auction findAuction) {
        return !(findAuction.isOwner(findUser) || findAuction.isWinner(findUser, LocalDateTime.now()));
    }

    public List<ReadMultipleChatRoomDto> readAllByUserId(final Long userId) {
        final User findUser = userRepository.getByIdOrThrow(userId);
        final List<MultipleChatRoomInfoDto> chatRoomAndMessageAndImageQueryProjectionDtos =
                multipleChatRoomRepository.findAllByUserIdOrderByLastMessage(findUser.getId());

        return chatRoomAndMessageAndImageQueryProjectionDtos.stream()
                                                            .map(dto -> ReadMultipleChatRoomDto.of(findUser, dto))
                                                            .toList();
    }

    public ReadSingleChatRoomDto readByChatRoomId(final Long chatRoomId, final Long userId) {
        final User findUser = userRepository.getByIdOrThrow(userId);
        final ChatRoom chatRoom = chatRoomRepository.getDetailChatRoomByIdOrThrow(chatRoomId);

        checkAccessible(findUser, chatRoom);

        return ReadSingleChatRoomDto.of(findUser, chatRoom);
    }

    private void checkAccessible(final User findUser, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(findUser)) {
            throw new InvalidUserToChat("해당 채팅방에 접근할 권한이 없습니다.");
        }
    }

    public com.ddang.ddang.auction.application.dto.ReadChatRoomDto readChatInfoByAuctionId(final Long auctionId, final AuthenticationUserInfo userInfo) {
        return userRepository.findById(userInfo.userId())
                .map(findUser -> convertReadChatRoomDto(auctionId, findUser))
                .orElse(com.ddang.ddang.auction.application.dto.ReadChatRoomDto.CANNOT_CHAT_DTO);
    }

    private com.ddang.ddang.auction.application.dto.ReadChatRoomDto convertReadChatRoomDto(final Long auctionId, final User findUser) {
        final Auction findAuction = auctionRepository.getTotalAuctionByIdOrThrow(auctionId);
        final Long chatRoomId = chatRoomRepository.findChatRoomIdByAuctionId(findAuction.getId())
                                                  .orElse(DEFAULT_CHAT_ROOM_ID);

        return new com.ddang.ddang.auction.application.dto.ReadChatRoomDto(chatRoomId, isChatParticipant(findAuction, findUser));
    }

    private boolean isChatParticipant(final Auction findAuction, final User findUser) {
        return findAuction.isClosed(LocalDateTime.now()) && findAuction.isSellerOrWinner(findUser, LocalDateTime.now());
    }
}
