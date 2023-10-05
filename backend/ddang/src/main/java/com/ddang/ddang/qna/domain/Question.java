package com.ddang.ddang.qna.domain;

import com.ddang.ddang.auction.domain.Auction;
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
public class Question extends BaseCreateTimeEntity {

    private static final boolean DELETED_STATUS = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", foreignKey = @ForeignKey(name = "fk_question_auction"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(columnDefinition = "text")
    private String content;

    @OneToOne(mappedBy = "question")
    private Answer answer;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    public Question(final Auction auction, final User writer, final String content) {
        this.auction = auction;
        this.writer = writer;
        this.content = content;
    }

    public void addAnswer(final Answer answer) {
        this.answer = answer;
        answer.initQuestion(this);
    }

    public boolean isAnsweringAllowed(final User user) {
        return auction.isOwner(user);
    }

    public boolean isWriter(final User user) {
        return writer.equals(user);
    }

    public void delete() {
        deleted = DELETED_STATUS;
    }
}
