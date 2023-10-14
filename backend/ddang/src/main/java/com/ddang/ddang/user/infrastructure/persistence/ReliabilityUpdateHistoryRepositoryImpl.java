package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import com.ddang.ddang.user.domain.repository.ReliabilityUpdateHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReliabilityUpdateHistoryRepositoryImpl implements ReliabilityUpdateHistoryRepository {

    private final JpaReliabilityUpdateHistoryRepository jpaReliabilityUpdateHistoryRepository;

    @Override
    public ReliabilityUpdateHistory save(final ReliabilityUpdateHistory reliabilityUpdateHistory) {
        return jpaReliabilityUpdateHistoryRepository.save(reliabilityUpdateHistory);
    }

    @Override
    public Optional<ReliabilityUpdateHistory> findFirstByOrderByIdDesc() {
        return jpaReliabilityUpdateHistoryRepository.findFirstByOrderByIdDesc();
    }
}
