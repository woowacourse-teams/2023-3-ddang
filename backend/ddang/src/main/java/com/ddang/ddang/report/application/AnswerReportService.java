package com.ddang.ddang.report.application;

import com.ddang.ddang.qna.application.exception.AnswerNotFoundException;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.infrastructure.JpaAnswerRepository;
import com.ddang.ddang.report.application.dto.CreateAnswerReportDto;
import com.ddang.ddang.report.application.dto.ReadAnswerReportDto;
import com.ddang.ddang.report.application.exception.InvalidAnswererReportException;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAnswerReportRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerReportService {

    private final JpaAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final JpaAnswerReportRepository answerReportRepository;

    @Transactional
    public Long create(final CreateAnswerReportDto answerReportDto) {
        final Answer answer = answerRepository.findByIdAndDeletedIsFalse(answerReportDto.answerId())
                                              .orElseThrow(() ->
                                                      new AnswerNotFoundException("해당 답변을 찾을 수 없습니다.")
                                              );
        final User reporter = userRepository.findByIdAndDeletedIsFalse(answerReportDto.reporterId())
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        checkInvalidAnswerReport(reporter, answer);

        final AnswerReport answerReport = answerReportDto.toEntity(answer, reporter);

        return answerReportRepository.save(answerReport)
                                     .getId();
    }

    private void checkInvalidAnswerReport(final User reporter, final Answer answer) {
        if (answer.isWriter(reporter)) {
            throw new InvalidAnswererReportException("본인 답변입니다.");
        }
        if (answerReportRepository.existsByAnswerIdAndReporterId(answer.getId(), reporter.getId())) {
            throw new InvalidAnswererReportException("이미 신고한 답변입니다.");
        }
    }

    public List<ReadAnswerReportDto> readAll() {
        final List<AnswerReport> answerReports = answerReportRepository.findAllByOrderByIdAsc();

        return answerReports.stream()
                            .map(ReadAnswerReportDto::from)
                            .toList();
    }
}
