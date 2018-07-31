package com.meksula.chat.repository;

import com.meksula.chat.domain.chat.dto.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author
 * Karol Meksuła
 * 31-07-2018
 * */

public interface ChatMessagesRepository extends JpaRepository<ChatMessage, Long> {
}
