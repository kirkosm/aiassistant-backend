package com.chatbot.aiassistant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ChatThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

    @PrePersist
    public void setCreatedAtBeforeSave() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
