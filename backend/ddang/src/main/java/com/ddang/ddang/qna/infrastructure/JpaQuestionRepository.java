package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.qna.domain.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaQuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = {"writer", "answer", "auction", "auction.seller"})
    List<Question> readAllByAuctionId(final Long auctionId);
}
