package com.ddang.ddang.qna.domain.repository;

import com.ddang.ddang.qna.domain.Answer;

import java.util.Optional;

public interface AnswerRepository {

    Answer save(final Answer answer);

    boolean existsByQuestionId(final Long questionId);

    Optional<Answer> findById(final Long id);
}
