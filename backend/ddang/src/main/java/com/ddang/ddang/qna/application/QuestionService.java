package com.ddang.ddang.qna.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.qna.application.dto.CreateQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadQnasDto;
import com.ddang.ddang.qna.application.event.QuestionNotificationEvent;
import com.ddang.ddang.qna.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.qna.application.exception.InvalidQuestionerException;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final ApplicationEventPublisher questionEventPublisher;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Long create(final CreateQuestionDto questionDto, final String absoluteImageUrl) {
        final User questioner = userRepository.getByIdOrThrow(questionDto.userId());
        final Auction auction = auctionRepository.getPureAuctionByIdOrThrow(questionDto.auctionId());

        checkInvalidAuction(auction);
        checkInvalidQuestioner(auction, questioner);

        final Question question = questionDto.toEntity(auction, questioner);

        final Question persistQuestion = questionRepository.save(question);

        questionEventPublisher.publishEvent(new QuestionNotificationEvent(persistQuestion, absoluteImageUrl));

        return persistQuestion.getId();
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

    public ReadQnasDto readAllByAuctionId(final Long auctionId, final Long userId) {
        if (!auctionRepository.existsById(auctionId)) {
            throw new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
        }

        final User user = userRepository.findById(userId)
                                        .orElse(User.EMPTY_USER);
        final List<Question> questions = questionRepository.findAllByAuctionId(auctionId);

        return ReadQnasDto.of(questions, user);
    }

    @Transactional
    public void deleteById(final Long questionId, final Long userId) {
        final Question question = questionRepository.getByIdOrThrow(questionId);
        final User user = userRepository.getByIdOrThrow(userId);

        if (!question.isWriter(user)) {
            throw new UserForbiddenException("삭제할 권한이 없습니다.");
        }

        question.delete();
    }
}
