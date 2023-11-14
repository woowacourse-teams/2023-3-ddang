package com.ddang.ddang.qna.infrastructure.persistence;

import com.ddang.ddang.qna.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByQuestionId(Long questionId);

    @Query("""
        SELECT an
        FROM Answer an
        JOIN FETCH an.question q
        JOIN FETCH q.auction a
        JOIN FETCH a.seller
        WHERE an.deleted = false AND an.id = :answerId
    """)
    Optional<Answer> findByIdAndDeletedIsFalse(final Long answerId);
}
