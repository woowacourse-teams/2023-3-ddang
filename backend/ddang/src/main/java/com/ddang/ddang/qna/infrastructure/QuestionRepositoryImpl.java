package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    public Question save(final Question question) {
        return jpaQuestionRepository.save(question);
    }

    @Override
    public Question getByIdOrThrow(final Long id) {
        return jpaQuestionRepository.findByIdAndDeletedIsFalse(id)
                                    .orElseThrow(() ->
                                            new QuestionNotFoundException("지정한 질문을 찾을 수 없습니다.")
                                    );
    }

    @Override
    public List<Question> findAllByAuctionId(final Long auctionId) {
        return jpaQuestionRepository.findAllByAuctionId(auctionId);
    }
}
