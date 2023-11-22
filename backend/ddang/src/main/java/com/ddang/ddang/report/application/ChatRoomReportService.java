package com.ddang.ddang.report.application;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportChatRoomException;
import com.ddang.ddang.report.application.exception.InvalidChatRoomReportException;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.domain.repository.ChatRoomReportRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomReportService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomReportRepository chatRoomReportRepository;

    @Transactional
    public Long create(final CreateChatRoomReportDto chatRoomReportDto) {
        final User reporter = userRepository.getByIdOrThrow(chatRoomReportDto.reporterId());
        final ChatRoom chatRoom = chatRoomRepository.getSimpleChatRoomByIdOrThrow(chatRoomReportDto.chatRoomId());

        checkInvalidChatRoomReport(reporter, chatRoom);

        final ChatRoomReport chatRoomReport = chatRoomReportDto.toEntity(reporter, chatRoom);

        return chatRoomReportRepository.save(chatRoomReport)
                                       .getId();
    }

    private void checkInvalidChatRoomReport(final User reporter, final ChatRoom chatRoom) {
        if (!chatRoom.isParticipant(reporter)) {
            throw new InvalidChatRoomReportException("해당 채팅방을 신고할 권한이 없습니다.");
        }
        if (chatRoomReportRepository.existsByChatRoomIdAndReporterId(chatRoom.getId(), reporter.getId())) {
            throw new AlreadyReportChatRoomException("이미 신고한 채팅방입니다.");
        }
    }

    public List<ReadChatRoomReportDto> readAll() {
        final List<ChatRoomReport> auctionReports = chatRoomReportRepository.findAll();

        return auctionReports.stream()
                             .map(ReadChatRoomReportDto::from)
                             .toList();
    }
}
