package com.ddang.ddang.qna.domain.repository;

import com.ddang.ddang.qna.domain.Question;
import java.util.List;

public interface QuestionRepository {

    Question save(final Question question);

    Question getByIdOrThrow(final Long id);

    List<Question> findAllByAuctionId(final Long auctionId);
}
