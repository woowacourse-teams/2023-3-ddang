package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByQuestionId(Long questionId);

    @EntityGraph(attributePaths = {"question", "question.auction", "question.auction.seller"})
    Optional<Answer> findByIdAndDeletedIsFalse(Long answerId);
}
