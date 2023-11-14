package com.ddang.ddang.qna.infrastructure.persistence;

import com.ddang.ddang.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaQuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndDeletedIsFalse(final Long id);

    @Query("""
        SELECT q
        FROM Question q
        JOIN FETCH q.writer
        LEFT JOIN FETCH q.answer
        JOIN FETCH q.auction a
        JOIN FETCH a.seller
        WHERE q.deleted = false AND a.id = :auctionId
    """)
    List<Question> findAllByAuctionId(final Long auctionId);
}
