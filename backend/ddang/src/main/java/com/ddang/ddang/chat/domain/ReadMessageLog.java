package com.ddang.ddang.chat.domain;

import com.ddang.ddang.user.domain.User;
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
@ToString(of = "id")
public class ReadMessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_message_log_chat_room"))
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_message_log_reader"))
    private User reader;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_message_log_last_read_message"))
    private Message lastReadMessage;

    public ReadMessageLog(final ChatRoom chatRoom, final User reader, final Message lastReadMessage) {
        this.chatRoom = chatRoom;
        this.reader = reader;
        this.lastReadMessage = lastReadMessage;
    }

    public void updateLastReadMessage(final Message lastReadMessage) {
        this.lastReadMessage = lastReadMessage;
    }
}
