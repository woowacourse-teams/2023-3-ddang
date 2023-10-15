package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.MessageRepositoryImplFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
public class MessageRepositoryImplTest extends MessageRepositoryImplFixture {

    MessageRepository messageRepository;

    @BeforeEach
    void setUp(
            @Autowired final JPAQueryFactory queryFactory,
            @Autowired final JpaMessageRepository jpaMessageRepository
    ) {
        messageRepository = new MessageRepositoryImpl(jpaMessageRepository, new QuerydslMessageRepository(queryFactory));
    }

    @Test
    void 메시지를_저장한다() {
        // when
        messageRepository.save(저장할_메시지);

        // then
        assertThat(저장할_메시지.getId()).isPositive();
    }

    @Test
    void 메시지_아이디에_해당하는_메시지가_존재하면_true를_반환한다() {
        // when
        final boolean actual = messageRepository.existsById(저장된_메시지.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 메시지_아이디에_해당하는_메시지가_존재하지_않으면_false를_반환한다() {
        // when
        final boolean actual = messageRepository.existsById(존재하지_않는_메시지_아이디);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 마지막_메시지_이후에_저장된_모든_메시지를_조회한다() {
        // when
        final List<Message> actual = messageRepository.findAllByLastMessageId(구매자.getId(), 메시지가_5개인_채팅방.getId(), 두_번째_메시지_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(세_번째_메시지);
            softAssertions.assertThat(actual.get(1)).isEqualTo(네_번째_메시지);
            softAssertions.assertThat(actual.get(2)).isEqualTo(다섯_번째_메시지);
        });
    }
}
