package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
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

    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaUserRepository userRepository;

    public List<ReadParticipatingChatRoomDto> readAllByUserId(final Long userId) {
        final User findUser = findUser(userId);
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(findUser.getId());

        return chatRooms.stream()
                        .map(chatRoom -> toDto(findUser, chatRoom))
                        .toList();
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
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
