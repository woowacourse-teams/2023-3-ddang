package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatAlreadyExistException;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaUserRepository userRepository;
    private final JpaAuctionRepository auctionRepository;

    public Long create(final Long userId, final CreateChatRoomDto chatRoomDto) {
        final User findUser = findUser(userId);
        final Auction findAuction = findAuction(chatRoomDto);

        checkAuctionStatus(findAuction);
        checkUserCanParticipate(findUser, findAuction);
        checkAlreadyExist(findAuction);

        final ChatRoom chatRoom = chatRoomDto.toEntity(findAuction);

        return chatRoomRepository.save(chatRoom)
                                 .getId();
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
    }

    private Auction findAuction(final CreateChatRoomDto chatRoomDto) {
        return auctionRepository.findById(chatRoomDto.auctionId())
                                .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));
    }

    private void checkAuctionStatus(final Auction findAuction) {
        if (!findAuction.isClosed(LocalDateTime.now())) {
            throw new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다.");
        }
        if (findAuction.isDeleted()) {
            throw new InvalidAuctionToChatException("삭제된 경매입니다.");
        }
    }

    private void checkUserCanParticipate(final User findUser, final Auction findAuction) {
        if (!findAuction.isOwner(findUser) && !findAuction.isWinner(findUser, LocalDateTime.now())) {
            throw new UserNotAccessibleException("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");
        }
    }

    private void checkAlreadyExist(final Auction findAuction) {
        final Optional<ChatRoom> nullableChatRoom = chatRoomRepository.findByAuctionId(findAuction.getId());
        if (nullableChatRoom.isPresent()) {
            throw new ChatAlreadyExistException("해당 경매에 대한 채팅방이 이미 존재합니다.");
        }
    }

    public List<ReadParticipatingChatRoomDto> readAllByUserId(final Long userId) {
        final User findUser = findUser(userId);
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(findUser.getId());

        return chatRooms.stream()
                        .map(chatRoom -> toDto(findUser, chatRoom))
                        .toList();
    }

    public ReadParticipatingChatRoomDto readByChatRoomId(final Long chatRoomId, final Long userId) {
        final User findUser = findUser(userId);
        final ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                                                    .orElseThrow(() ->
                                                            new ChatRoomNotFoundException(
                                                                    "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."
                                                            )
                                                    );
        checkAccessible(findUser, chatRoom);

        return toDto(findUser, chatRoom);
    }

    private void checkAccessible(final User findUser, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(findUser)) {
            throw new UserNotAccessibleException("해당 채팅방에 접근할 권한이 없습니다.");
        }
    }

    private ReadParticipatingChatRoomDto toDto(final User findUser, final ChatRoom chatRoom) {
        return ReadParticipatingChatRoomDto.of(
                chatRoom.calculateChatPartnerOf(findUser),
                chatRoom,
                chatRoom.isChatAvailableTime(LocalDateTime.now()));
    }
}
