package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final JpaMessageRepository messageRepository;
    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public Long create(final CreateMessageDto dto) {
        final ChatRoom chatRoom = findChatRoom(dto.chatRoomId(), "지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        final User writer = findUser(dto.writerId(), "지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
        final User receiver = findUser(dto.receiverId(), "지정한 아이디에 대한 수신자를 찾을 수 없습니다.");
        final Message message = dto.toEntity(chatRoom, writer, receiver);

        return messageRepository.save(message)
                                .getId();
    }

    private ChatRoom findChatRoom(final Long chatRoomId, final String message) {
        return chatRoomRepository.findById(chatRoomId)
                                 .orElseThrow(() -> new ChatRoomNotFoundException(message));
    }

    private User findUser(final Long dto, final String message) {
        return userRepository.findById(dto)
                             .orElseThrow(() -> new UserNotFoundException(message));
    }

    public List<ReadMessageDto> readAllByLastMessageId(final ReadMessageRequest request) {
        final User user = findUser(request.userId(), "메시지 조회할 권한이 없는 사용자입니다.");
        final ChatRoom chatRoom = findChatRoom(request.chatRoomId(), "조회하고자 하는 채팅방이 존재하지 않습니다.");

        Long lastMessageId = request.lastMessageId();
        if (lastMessageId != null) {
            lastMessageId = findMessage(lastMessageId, "조회한 마지막 메시지가 존재하지 않습니다.").getId();
            findMessage(request.lastMessageId(), "조회한 마지막 메시지가 존재하지 않습니다.");
        }

        final List<Message> readMessages = messageRepository.findMessagesAllByLastMessageId(
                user.getId(),
                chatRoom.getId(),
                lastMessageId
        );

        return readMessages.stream()
                           .map(ReadMessageDto::from)
                           .collect(Collectors.toList());
    }

    private Message findMessage(final Long messageId, final String message) {
        return messageRepository.findById(messageId)
                                .orElseThrow(() -> new MessageNotFoundException(message));
    }
}
