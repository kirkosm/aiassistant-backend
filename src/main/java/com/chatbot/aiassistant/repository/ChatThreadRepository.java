package com.chatbot.aiassistant.repository;

import com.chatbot.aiassistant.model.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {
    List<ChatThread> findByUserId(Long userId);
}
