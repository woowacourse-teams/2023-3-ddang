package com.ddang.ddang.report.application;

import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.report.application.dto.CreateQuestionReportDto;
import com.ddang.ddang.report.application.dto.ReadQuestionReportDto;
import com.ddang.ddang.report.application.exception.InvalidQuestionReportException;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaQuestionReportRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionReportService {

    private final JpaQuestionRepository questionRepository;
    private final JpaUserRepository userRepository;
    private final JpaQuestionReportRepository questionReportRepository;

    @Transactional
    public Long create(final CreateQuestionReportDto questionReportDto) {
        final Question question = questionRepository.findByIdAndDeletedIsFalse(questionReportDto.questionId())
                                                    .orElseThrow(() ->
                                                            new QuestionNotFoundException("해당 질문을 찾을 수 없습니다.")
                                                    );
        final User reporter = userRepository.findByIdAndDeletedIsFalse(questionReportDto.reporterId())
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        checkInvalidQuestionReport(reporter, question);

        final QuestionReport questionReport = questionReportDto.toEntity(question, reporter);

        return questionReportRepository.save(questionReport)
                                       .getId();
    }

    private void checkInvalidQuestionReport(final User reporter, final Question question) {
        if (question.isWriter(reporter)) {
            throw new InvalidQuestionReportException("본인 질문입니다.");
        }
        if (questionReportRepository.existsByIdAndReporterId(question.getId(), reporter.getId())) {
            throw new InvalidQuestionReportException("이미 신고한 질문입니다.");
        }
    }

    public List<ReadQuestionReportDto> readAll() {
        final List<QuestionReport> auctionReports = questionReportRepository.findAllByOrderByIdAsc();

        return auctionReports.stream()
                             .map(ReadQuestionReportDto::from)
                             .toList();
    }
}