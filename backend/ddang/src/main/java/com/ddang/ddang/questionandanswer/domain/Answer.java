package com.ddang.ddang.questionandanswer.domain;

import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "content"})
public class Answer extends BaseCreateTimeEntity {

    // TODO: 2023-10-04 [고민] answer에 writer를 만들어 바로 가져오는 방식이 좋을까요?
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_question"))
    private Question question;

    @Column(columnDefinition = "text")
    private String content;

    public Answer(final String content) {
        this.content = content;
    }

    public void initQuestion(final Question question) {
        this.question = question;
    }
}
