package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UnableToChatException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final ApplicationEventPublisher messageLogEventPublisher;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Message create(final CreateMessageDto dto) {
        final ChatRoom chatRoom = chatRoomRepository.findById(dto.chatRoomId())
                                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                                            "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."
                                                    ));
        final User writer = userRepository.findByIdWithProfileImage(dto.writerId())
                                          .orElseThrow(() -> new UserNotFoundException(
                                                  "지정한 아이디에 대한 발신자를 찾을 수 없습니다."
                                          ));
        final User receiver = userRepository.findById(dto.receiverId())
                                            .orElseThrow(() -> new UserNotFoundException(
                                                    "지정한 아이디에 대한 수신자를 찾을 수 없습니다."
                                            ));
        validateReceiver(chatRoom, receiver);

        final Message message = dto.toEntity(chatRoom, writer, receiver);

        return messageRepository.save(message);
    }

    private void validateReceiver(final ChatRoom chatRoom, final User receiver) {
        if (!chatRoom.isChatAvailablePartner(receiver)) {
            throw new UnableToChatException("탈퇴한 사용자에게는 메시지 전송이 불가능합니다.");
        }
    }

    public List<ReadMessageDto> readAllByLastMessageId(final ReadMessageRequest request) {
        final User reader = userRepository.findById(request.messageReaderId())
                                          .orElseThrow(() -> new UserNotFoundException("지정한 아이디에 대한 사용자를 찾을 수 없습니다."));
        final ChatRoom chatRoom = chatRoomRepository.findById(request.chatRoomId())
                                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                                            "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        if (request.lastMessageId() != null) {
            validateLastMessageId(request.lastMessageId());
        }

        final List<Message> readMessages = messageRepository.findAllByLastMessageId(
                request.messageReaderId(),
                chatRoom.getId(),
                request.lastMessageId()
        );

        if (!readMessages.isEmpty()) {
            final Message lastReadMessage = readMessages.get(readMessages.size() - 1);

            messageLogEventPublisher.publishEvent(new UpdateReadMessageLogEvent(reader, chatRoom, lastReadMessage));
        }

        return readMessages.stream()
                           .map(message -> ReadMessageDto.of(message, chatRoom))
                           .toList();
    }

    private void validateLastMessageId(final Long lastMessageId) {
        if (!messageRepository.existsById(lastMessageId)) {
            throw new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다.");
        }
    }
}
