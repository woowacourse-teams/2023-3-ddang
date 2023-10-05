package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByQuestionId(Long questionId);
}
