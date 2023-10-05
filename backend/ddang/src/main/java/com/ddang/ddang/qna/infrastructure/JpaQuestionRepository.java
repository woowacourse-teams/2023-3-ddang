package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaQuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndDeletedIsFalse(final Long id);

    @EntityGraph(attributePaths = {"writer", "answer", "auction", "auction.seller"})
    List<Question> findAllByAuctionId(final Long auctionId);
}
