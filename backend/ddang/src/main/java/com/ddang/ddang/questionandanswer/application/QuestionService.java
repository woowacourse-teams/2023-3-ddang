package com.ddang.ddang.questionandanswer.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.questionandanswer.application.dto.CreateQuestionDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswersDto;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidQuestionerException;
import com.ddang.ddang.questionandanswer.domain.Question;
import com.ddang.ddang.questionandanswer.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final JpaAuctionRepository auctionRepository;
    private final JpaUserRepository userRepository;
    private final JpaQuestionRepository questionRepository;

    public Long create(final CreateQuestionDto questionDto) {
        final User questioner = userRepository.findByIdAndDeletedIsFalse(questionDto.userId())
                                              .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final Auction auction = auctionRepository.findByIdAndDeletedIsFalse(questionDto.auctionId())
                                                 .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        checkInvalidAuction(auction);
        checkInvalidQuestioner(auction, questioner);

        final Question question = questionDto.toEntity(auction, questioner);

        return questionRepository.save(question)
                                 .getId();
    }

    private void checkInvalidAuction(final Auction auction) {
        if (auction.isClosed(LocalDateTime.now())) {
            throw new InvalidAuctionToAskQuestionException("이미 종료된 경매입니다.");
        }
    }

    private void checkInvalidQuestioner(final Auction auction, final User questioner) {
        if (auction.isOwner(questioner)) {
            throw new InvalidQuestionerException("경매 등록자는 질문할 수 없습니다.");
        }
    }

    public ReadQuestionAndAnswersDto readAllByAuctionId(final Long auctionId) {
        if (!auctionRepository.existsById(auctionId)) {
            throw new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
        }

        final List<Question> questions = questionRepository.readAllByAuctionId(auctionId);

        return ReadQuestionAndAnswersDto.from(questions);
    }
}
