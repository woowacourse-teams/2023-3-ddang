package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaMessageRepository messageRepository;
    private final JpaUserRepository userRepository;
    private final JpaAuctionRepository auctionRepository;

    @Transactional
    public Long create(final Long userId, final CreateChatRoomDto chatRoomDto) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final Auction findAuction = auctionRepository.findById(chatRoomDto.auctionId())
                                                     .orElseThrow(() ->
                                                             new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        final ChatRoom persistChatRoom = findOrCreateChatRoomByAuction(findUser, findAuction);

        return persistChatRoom.getId();
    }

    private ChatRoom findOrCreateChatRoomByAuction(final User user, final Auction auction) {
        return chatRoomRepository.findByAuctionId(auction.getId())
                                 .orElseGet(() -> createAndSaveChatRoom(user, auction));
    }

    private ChatRoom createAndSaveChatRoom(final User user, final Auction auction) {
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
        if (findAuction.isDeleted()) {
            throw new InvalidAuctionToChatException("삭제된 경매입니다.");
        }
    }

    private void checkUserCanParticipate(final User findUser, final Auction findAuction) {
        if (!isSellerOrWinner(findUser, findAuction)) {
            throw new UserNotAccessibleException("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");
        }
    }

    private boolean isSellerOrWinner(final User findUser, final Auction findAuction) {
        return findAuction.isOwner(findUser) || findAuction.isWinner(findUser, LocalDateTime.now());
    }

    public List<ReadChatRoomWithLastMessageDto> readAllByUserId(final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(findUser.getId());

        return processChatRoomWithLastMessageAndSort(findUser, chatRooms);
    }

    private List<ReadChatRoomWithLastMessageDto> processChatRoomWithLastMessageAndSort(final User findUser, final List<ChatRoom> chatRooms) {
        List<ReadChatRoomWithLastMessageDto> chatRoomDtos = new ArrayList<>();
        for (final ChatRoom chatRoom : chatRooms) {
            messageRepository.findLastMessageByChatRoomId(chatRoom.getId())
                             .ifPresent(message ->
                                     chatRoomDtos.add(ReadChatRoomWithLastMessageDto.of(findUser, chatRoom, message))
                             );
        }

        return chatRoomDtos.stream()
                           .sorted(comparing((ReadChatRoomWithLastMessageDto dto) -> dto.lastMessageDto().createdTime())
                                   .reversed())
                           .toList();
    }

    public ReadParticipatingChatRoomDto readByChatRoomId(final Long chatRoomId, final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId)
                                                    .orElseThrow(() ->
                                                            new ChatRoomNotFoundException(
                                                                    "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."
                                                            )
                                                    );
        checkAccessible(findUser, chatRoom);

        return ReadParticipatingChatRoomDto.of(findUser, chatRoom, LocalDateTime.now());
    }

    private void checkAccessible(final User findUser, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(findUser)) {
            throw new UserNotAccessibleException("해당 채팅방에 접근할 권한이 없습니다.");
        }
    }
}
