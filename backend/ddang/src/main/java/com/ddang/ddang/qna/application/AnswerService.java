package com.ddang.ddang.qna.application;

import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.qna.application.dto.CreateAnswerDto;
import com.ddang.ddang.qna.application.event.AnswerNotificationEvent;
import com.ddang.ddang.qna.application.exception.AlreadyAnsweredException;
import com.ddang.ddang.qna.application.exception.InvalidAnswererException;
import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {

    private final ApplicationEventPublisher answerEventPublisher;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public Long create(final CreateAnswerDto answerDto, final String absoluteImageUrl) {
        final User writer = userRepository.findById(answerDto.userId())
                                          .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final Question question = questionRepository.findById(answerDto.questionId())
                                                    .orElseThrow(() ->
                                                            new QuestionNotFoundException("해당 질문을 찾을 수 없습니다.")
                                                    );

        checkInvalidAnswerer(question, writer);
        checkAlreadyAnswered(question);

        final Answer answer = answerDto.toEntity(writer);
        question.addAnswer(answer);

        final Answer persistAnswer = answerRepository.save(answer);

        answerEventPublisher.publishEvent(new AnswerNotificationEvent(persistAnswer, absoluteImageUrl));

        return persistAnswer.getId();
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

    @Transactional
    public void deleteById(final Long answerId, final Long userId) {
        final Answer answer = answerRepository.getByIdOrThrow(answerId);
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        if (!answer.isWriter(user)) {
            throw new UserForbiddenException("삭제할 권한이 없습니다.");
        }

        answer.delete();
    }
}
