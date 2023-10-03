package com.ddang.ddang.questionandanswer.infrastructure;

import com.ddang.ddang.questionandanswer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {
}
