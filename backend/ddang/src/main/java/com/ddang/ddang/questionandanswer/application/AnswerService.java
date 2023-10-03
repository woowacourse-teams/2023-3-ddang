package com.ddang.ddang.questionandanswer.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.questionandanswer.application.dto.CreateAnswerDto;
import com.ddang.ddang.questionandanswer.application.exception.AlreadyAnsweredException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAnswererException;
import com.ddang.ddang.questionandanswer.domain.Answer;
import com.ddang.ddang.questionandanswer.domain.Question;
import com.ddang.ddang.questionandanswer.infrastructure.JpaAnswerRepository;
import com.ddang.ddang.questionandanswer.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {

    private final JpaUserRepository userRepository;
    private final JpaQuestionRepository questionRepository;
    private final JpaAnswerRepository answerRepository;


    @Transactional
    public Long create(final CreateAnswerDto answerDto) {
        final User writer = userRepository.findById(answerDto.userId())
                                          .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final Question question = questionRepository.findById(answerDto.questionId())
                                                    .orElseThrow(() ->
                                                            new AuctionNotFoundException("해당 질문을 찾을 수 없습니다.")
                                                    );

        checkInvalidAnswerer(question, writer);
        checkAlreadyAnswered(question);

        final Answer answer = answerDto.toEntity();
        question.addAnswer(answer);

        return answerRepository.save(answer)
                               .getId();
    }

    private void checkInvalidAnswerer(final Question question, final User writer) {
        if (!question.isAnsweringAllowed(writer)) {
            throw new InvalidAnswererException("판매자만 답변할 수 있습니다.");
        }
    }

    private void checkAlreadyAnswered(final Question question) {
        if (answerRepository.existsByQuestionId(question.getId())) {
            throw new AlreadyAnsweredException("이미 답변한 질문입니다.");
        }
    }
}
