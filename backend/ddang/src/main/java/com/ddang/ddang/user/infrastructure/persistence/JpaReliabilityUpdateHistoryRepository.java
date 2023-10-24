package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaReliabilityUpdateHistoryRepository extends JpaRepository<ReliabilityUpdateHistory, Long> {

    Optional<ReliabilityUpdateHistory> findFirstByOrderByIdDesc();
}
