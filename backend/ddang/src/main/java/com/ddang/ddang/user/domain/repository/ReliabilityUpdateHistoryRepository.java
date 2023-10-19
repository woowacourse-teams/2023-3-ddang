package com.ddang.ddang.user.domain.repository;

import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;

import java.util.Optional;

public interface ReliabilityUpdateHistoryRepository {

    ReliabilityUpdateHistory save(final ReliabilityUpdateHistory reliabilityUpdateHistory);

    Optional<ReliabilityUpdateHistory> findFirstByOrderByIdDesc();
}
