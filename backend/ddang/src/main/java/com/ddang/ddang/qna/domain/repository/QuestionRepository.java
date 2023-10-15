package com.ddang.ddang.qna.domain.repository;

import com.ddang.ddang.qna.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Question save(final Question question);

    Optional<Question> findById(final Long id);

    List<Question> findAllByAuctionId(final Long auctionId);
}
