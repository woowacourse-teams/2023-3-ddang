package com.ddang.ddang.qna.domain;

import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
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
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "content", "deleted"})
public class Answer extends BaseCreateTimeEntity {

    private static final boolean DELETED_STATUS = true;
    private static final Question EMPTY_QUESTION = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_question"))
    private Question question;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    public Answer(final User writer, final String content) {
        this.writer = writer;
        this.content = content;
    }

    public void initQuestion(final Question question) {
        this.question = question;
    }

    public boolean isWriter(final User user) {
        return writer.equals(user);
    }

    public void delete() {
        deleted = DELETED_STATUS;

        if (question != EMPTY_QUESTION) {
            question.deleteAnswer();
            question = EMPTY_QUESTION;
        }
    }
}
