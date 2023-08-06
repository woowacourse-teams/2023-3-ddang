package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<Message, Long> {
}
