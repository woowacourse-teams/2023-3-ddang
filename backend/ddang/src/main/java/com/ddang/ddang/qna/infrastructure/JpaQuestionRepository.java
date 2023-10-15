package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaQuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndDeletedIsFalse(final Long id);

    @Query("""
        select q
        from Question q
        join fetch q.writer
        join fetch q.answer
        join fetch q.auction a
        join fetch a.seller
        where a.id = :id
    """)
    List<Question> findAllByAuctionId(final Long auctionId);
}
