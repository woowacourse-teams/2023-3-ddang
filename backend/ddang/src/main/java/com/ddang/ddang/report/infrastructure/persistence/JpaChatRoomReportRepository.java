package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.ChatRoomReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRoomReportRepository extends JpaRepository<ChatRoomReport, Long> {
}
