package com.ddang.ddang.questionandanswer.infrastructure;

import com.ddang.ddang.questionandanswer.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionRepository extends JpaRepository<Question, Long> {
}
