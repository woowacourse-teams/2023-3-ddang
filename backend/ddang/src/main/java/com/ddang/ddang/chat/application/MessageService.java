package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UnableToChatException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.application.NotificationService;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final NotificationService notificationService;
    private final JpaMessageRepository messageRepository;
    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public Long create(final CreateMessageDto dto, final String absoluteUrl) {
        final ChatRoom chatRoom = chatRoomRepository.findById(dto.chatRoomId())
                                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                                            "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));
        final User writer = userRepository.findById(dto.writerId())
                                          .orElseThrow(() -> new UserNotFoundException(
                                                  "지정한 아이디에 대한 발신자를 찾을 수 없습니다."));
        final User receiver = userRepository.findById(dto.receiverId())
                                            .orElseThrow(() -> new UserNotFoundException(
                                                    "지정한 아이디에 대한 수신자를 찾을 수 없습니다."));

        if (!chatRoom.isChatAvailableTime(LocalDateTime.now())) {
            throw new UnableToChatException("현재 메시지 전송이 불가능합니다.");
        }

        final Message message = dto.toEntity(chatRoom, writer, receiver);

        final Message persistMessage = messageRepository.save(message);

        try {
            final String sendNotificationMessage = sendNotification(persistMessage, absoluteUrl);
            log.info(sendNotificationMessage);
        } catch (Exception ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }

        return persistMessage.getId();
    }

    private String sendNotification(final Message message, final String absoluteUrl)  {
        final ProfileImage writerProfileImage = message.getWriter().getProfileImage();

        final CreateNotificationDto dto = new CreateNotificationDto(
                NotificationType.MESSAGE,
                message.getReceiver().getId(),
                message.getWriter().getName(),
                message.getContents(),
                calculateRedirectUrl(message.getChatRoom().getId()),
                ImageUrlCalculator.calculateBy(absoluteUrl, writerProfileImage.getId())
        );

        return notificationService.send(dto);
    }

    private String calculateRedirectUrl(final Long id) {
        return "/chattings/" + id;
    }

    public List<ReadMessageDto> readAllByLastMessageId(final ReadMessageRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new UserNotFoundException("지정한 아이디에 대한 사용자를 찾을 수 없습니다.");
        }

        final ChatRoom chatRoom = chatRoomRepository.findById(request.chatRoomId())
                                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                                            "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        if (request.lastMessageId() != null) {
            validateLastMessageId(request.lastMessageId());
        }

        final List<Message> readMessages = messageRepository.findMessagesAllByLastMessageId(
                request.userId(),
                chatRoom.getId(),
                request.lastMessageId()
        );

        return readMessages.stream()
                           .map(message -> ReadMessageDto.from(message, chatRoom))
                           .toList();
    }

    private void validateLastMessageId(final Long lastMessageId) {
        if (!messageRepository.existsById(lastMessageId)) {
            throw new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다.");
        }
    }
}
