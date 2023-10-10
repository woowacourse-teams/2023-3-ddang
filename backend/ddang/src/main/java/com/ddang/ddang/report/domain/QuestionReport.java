package com.ddang.ddang.report.domain;

import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import com.ddang.ddang.qna.domain.Question;
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
public class QuestionReport extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "fk_question_report_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_question_report_question"))
    private Question question;

    @Column(columnDefinition = "text")
    private String description;

    public QuestionReport(final User reporter, final Question question, final String description) {
        this.reporter = reporter;
        this.question = question;
        this.description = description;
    }
}
