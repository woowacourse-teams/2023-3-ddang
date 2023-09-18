package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.InvalidUserToChat;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.QuerydslChatRoomAndMessageAndImageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private static final Long DEFAULT_CHAT_ROOM_ID = null;

    private final JpaChatRoomRepository chatRoomRepository;
    private final QuerydslChatRoomAndMessageAndImageRepository querydslChatRoomAndMessageAndImageRepository;
    private final JpaUserRepository userRepository;
    private final JpaAuctionRepository auctionRepository;

    @Transactional
    public Long create(final Long userId, final CreateChatRoomDto chatRoomDto) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final Auction findAuction = auctionRepository.findAuctionById(chatRoomDto.auctionId())
                                                     .orElseThrow(() ->
                                                             new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.")
                                                     );

        return chatRoomRepository.findChatRoomIdByAuctionId(findAuction.getId())
                                 .orElseGet(() ->
                                         persistChatRoom(findUser, findAuction).getId()
                                 );
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

    public List<ReadChatRoomWithLastMessageDto> readAllByUserId(final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final List<ChatRoomAndMessageAndImageDto> chatRoomAndMessageAndImageQueryProjectionDtos =
                querydslChatRoomAndMessageAndImageRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(findUser.getId());

        return chatRoomAndMessageAndImageQueryProjectionDtos.stream()
                                                            .map(dto -> ReadChatRoomWithLastMessageDto.of(findUser, dto))
                                                            .toList();
    }

    public ReadParticipatingChatRoomDto readByChatRoomId(final Long chatRoomId, final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId)
                                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                                            "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."
                                                    ));
        checkAccessible(findUser, chatRoom);

        return ReadParticipatingChatRoomDto.of(findUser, chatRoom, LocalDateTime.now());
    }

    private void checkAccessible(final User findUser, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(findUser)) {
            throw new InvalidUserToChat("해당 채팅방에 접근할 권한이 없습니다.");
        }
    }

    public ReadChatRoomDto readChatInfoByAuctionId(final Long auctionId, final AuthenticationUserInfo userInfo) {
        final User findUser = userRepository.findById(userInfo.userId())
                                            .orElseThrow(() -> new UserNotFoundException("회원 정보를 찾을 수 없습니다."));
        final Auction findAuction = auctionRepository.findAuctionById(auctionId)
                                                     .orElseThrow(() -> new AuctionNotFoundException(
                                                             "지정한 아이디에 대한 경매를 찾을 수 없습니다."
                                                     ));
        final Long chatRoomId = chatRoomRepository.findChatRoomIdByAuctionId(findAuction.getId())
                                                  .orElse(DEFAULT_CHAT_ROOM_ID);

        return new ReadChatRoomDto(chatRoomId, isChatParticipant(findAuction, findUser));
    }

    private boolean isChatParticipant(final Auction findAuction, final User findUser) {
        return findAuction.isClosed(LocalDateTime.now()) && findAuction.isSellerOrWinner(findUser, LocalDateTime.now());
    }
}
