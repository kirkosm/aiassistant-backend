package com.chatbot.aiassistant.repository;

import com.chatbot.aiassistant.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
