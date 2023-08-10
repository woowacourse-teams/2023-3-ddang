package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.ChatRoomReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaChatRoomReportRepository extends JpaRepository<ChatRoomReport, Long> {

    @Override
    @EntityGraph(attributePaths = {"reporter", "chatRoom", "chatRoom.buyer", "chatRoom.auction", "chatRoom.auction.seller"})
    List<ChatRoomReport> findAll();
}
