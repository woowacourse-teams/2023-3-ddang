package com.ddang.ddang.qna.domain.repository;

import com.ddang.ddang.qna.domain.Answer;

public interface AnswerRepository {

    Answer save(final Answer answer);

    boolean existsByQuestionId(final Long questionId);

    Answer getByIdOrThrow(final Long id);
}
