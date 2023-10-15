package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.domain.repository.ChatRoomReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomReportRepositoryImpl implements ChatRoomReportRepository {

    private final JpaChatRoomReportRepository jpaChatRoomReportRepository;

    @Override
    public ChatRoomReport save(final ChatRoomReport chatRoomReport) {
        return jpaChatRoomReportRepository.save(chatRoomReport);
    }

    @Override
    public boolean existsByChatRoomIdAndReporterId(final Long chatRoomId, final Long reporterId) {
        return jpaChatRoomReportRepository.existsByChatRoomIdAndReporterId(chatRoomId, reporterId);
    }

    @Override
    public List<ChatRoomReport> findAll() {
        return jpaChatRoomReportRepository.findAll();
    }
}
