package com.ddang.ddang.report.domain;

import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "description"})
public class AnswerReport extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "fk_answer_report_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", foreignKey = @ForeignKey(name = "fk_answer_report_answer"))
    private Answer answer;

    @Column(columnDefinition = "text")
    private String description;

    public AnswerReport(final User reporter, final Answer answer, final String description) {
        this.reporter = reporter;
        this.answer = answer;
        this.description = description;
    }
}
