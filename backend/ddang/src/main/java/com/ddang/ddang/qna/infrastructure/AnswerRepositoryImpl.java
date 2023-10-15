package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
    public Optional<Answer> findById(final Long id) {
        return jpaAnswerRepository.findByIdAndDeletedIsFalse(id);
    }
}
