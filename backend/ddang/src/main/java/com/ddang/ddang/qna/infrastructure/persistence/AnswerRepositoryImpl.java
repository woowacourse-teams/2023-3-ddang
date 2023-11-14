package com.ddang.ddang.qna.infrastructure.persistence;

import com.ddang.ddang.qna.infrastructure.exception.AnswerNotFoundException;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final JpaAnswerRepository jpaAnswerRepository;

    @Override
    public Answer save(final Answer answer) {
        return jpaAnswerRepository.save(answer);
    }

    @Override
    public boolean existsByQuestionId(final Long questionId) {
        return jpaAnswerRepository.existsByQuestionId(questionId);
    }

    @Override
    public Answer getByIdOrThrow(final Long id) {
        return jpaAnswerRepository.findByIdAndDeletedIsFalse(id)
                                  .orElseThrow(() -> new AnswerNotFoundException("지정한 답변을 찾을 수 없습니다."));
    }
}
