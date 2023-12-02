package com.ddang.ddang.qna.infrastructure;

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
        JOIN FETCH q.writer w
        LEFT JOIN FETCH w.profileImage
        LEFT JOIN FETCH q.answer
        JOIN FETCH q.auction a
        JOIN FETCH a.seller s
        JOIN FETCH s.profileImage
        WHERE q.deleted = false AND a.id = :auctionId
    """)
    List<Question> findAllByAuctionId(final Long auctionId);
}
