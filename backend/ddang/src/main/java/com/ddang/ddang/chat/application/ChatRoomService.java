package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaUserRepository userRepository;

    public List<ReadParticipatingChatRoomDto> readAllByUserId(final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(findUser.getId());
        return chatRooms.stream()
                        .map(chatRoom -> toDto(findUser, chatRoom))
                        .toList();
    }

    private ReadParticipatingChatRoomDto toDto(final User findUser, final ChatRoom chatRoom) {
        return ReadParticipatingChatRoomDto.of(chatRoom.calculateChatPartnerOf(findUser), chatRoom);
    }
}
