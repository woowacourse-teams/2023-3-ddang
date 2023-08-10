package com.ddang.ddang.report.application;

import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.exception.ChatRoomReportNotAccessibleException;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaChatRoomReportRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomReportService {

    private final JpaUserRepository userRepository;
    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaChatRoomReportRepository chatRoomReportRepository;


    public Long create(final CreateChatRoomReportDto chatRoomReportDto) {
        final User reporter = userRepository.findById(chatRoomReportDto.reporterId())
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final ChatRoom chatRoom =
                chatRoomRepository.findById(chatRoomReportDto.chatRoomId())
                                  .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));
        checkAccessible(reporter, chatRoom);

        ChatRoomReport chatRoomReport = chatRoomReportDto.toEntity(reporter, chatRoom);

        return chatRoomReportRepository.save(chatRoomReport)
                                       .getId();
    }

    private void checkAccessible(final User findUser, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(findUser)) {
            throw new ChatRoomReportNotAccessibleException("해당 채팅방을 신고할 권한이 없습니다.");
        }
    }
}
