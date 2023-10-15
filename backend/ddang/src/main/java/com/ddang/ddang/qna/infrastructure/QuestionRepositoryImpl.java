package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    public Question save(final Question question) {
        return jpaQuestionRepository.save(question);
    }

    @Override
    public Optional<Question> findById(final Long id) {
        return jpaQuestionRepository.findByIdAndDeletedIsFalse(id);
    }

    @Override
    public List<Question> findAllByAuctionId(final Long auctionId) {
        return jpaQuestionRepository.findAllByAuctionId(auctionId);
    }
}
