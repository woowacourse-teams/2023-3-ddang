package com.ddang.ddang.report.application;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.report.application.dto.CreateQuestionReportDto;
import com.ddang.ddang.report.application.dto.ReadQuestionReportDto;
import com.ddang.ddang.report.application.exception.InvalidQuestionReportException;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.domain.repository.QuestionReportRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionReportService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionReportRepository questionReportRepository;

    @Transactional
    public Long create(final CreateQuestionReportDto questionReportDto) {
        final Question question = questionRepository.getByIdOrThrow(questionReportDto.questionId());
        final User reporter = userRepository.getByIdOrThrow(questionReportDto.reporterId());

        checkInvalidQuestionReport(reporter, question);

        final QuestionReport questionReport = questionReportDto.toEntity(question, reporter);

        return questionReportRepository.save(questionReport)
                                       .getId();
    }

    private void checkInvalidQuestionReport(final User reporter, final Question question) {
        if (question.isWriter(reporter)) {
            throw new InvalidQuestionReportException("본인 질문입니다.");
        }
        if (questionReportRepository.existsByQuestionIdAndReporterId(question.getId(), reporter.getId())) {
            throw new InvalidQuestionReportException("이미 신고한 질문입니다.");
        }
    }

    public List<ReadQuestionReportDto> readAll() {
        final List<QuestionReport> questionReports = questionReportRepository.findAll();

        return questionReports.stream()
                              .map(ReadQuestionReportDto::from)
                              .toList();
    }
}
