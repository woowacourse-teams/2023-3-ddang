package com.ddang.ddang.chat.domain;

import com.ddang.ddang.user.domain.User;
import jakarta.persistence.CascadeType;
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
@ToString(of = {"id", "lastReadMessageId"})
public class ReadMessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_message_log_chat_room"))
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "reader_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_message_log_reader"))
    private User reader;

    private Long lastReadMessageId = 0L;

    public ReadMessageLog(final ChatRoom chatRoom, final User reader) {
        this.chatRoom = chatRoom;
        this.reader = reader;
    }

    public void updateLastReadMessage(final Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
