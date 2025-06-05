package com.chatbot.aiassistant.controller;

import com.chatbot.aiassistant.model.ChatMessage;
import com.chatbot.aiassistant.model.ChatThread;
import com.chatbot.aiassistant.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/start")
    public Map<String, Object> startNewChat(@RequestBody Map<String, Object> request) {
        String message = request.get("message").toString();
        Long userId = Long.parseLong(request.get("userId").toString());

        String response = chatService.startNewThread(userId, message);
        List<ChatThread> threads = chatService.getThreadsByUser(userId);

        if (threads.isEmpty()) throw new RuntimeException("Thread could not be created");

        ChatThread latest = threads.get(threads.size() - 1);

        Map<String, Object> result = new HashMap<>();
        result.put("threadId", latest.getId());
        result.put("messages", List.of(
                Map.of("sender", "user", "text", message),
                Map.of("sender", "bot", "text", response)
        ));
        return result;
    }

    @PostMapping("/send")
    public String sendMessageToThread(@RequestBody Map<String, Object> request) {
        String message = request.get("message").toString();
        Long threadId = Long.parseLong(request.get("threadId").toString());

        chatService.addMessageToThread(threadId, "user", message);

        String response = chatService.addMessageToThread(
                threadId, "bot",
                chatService.getGroqClient().getGroqResponse(message)
        );

        return response;
    }

    @GetMapping("/threads")
    public List<Map<String, Object>> getUserThreads(@RequestParam Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChatThread thread : chatService.getThreadsByUser(userId)) {
            result.add(Map.of(
                    "id", thread.getId(),
                    "title", thread.getTitle()
            ));
        }
        return result;
    }

    @GetMapping("/thread/{threadId}/messages")
    public List<Map<String, Object>> getThreadMessages(@PathVariable Long threadId) {
        return chatService.getThreadById(threadId)
                .map(thread -> {
                    List<Map<String, Object>> result = new ArrayList<>();
                    for (ChatMessage msg : thread.getMessages()) {
                        result.add(Map.of(
                                "sender", msg.getSender(),
                                "content", msg.getContent()
                        ));
                    }
                    return result;
                })
                .orElse(List.of());
    }

    @DeleteMapping("/thread/{threadId}")
    public void deleteThread(@PathVariable Long threadId) {
        chatService.deleteThread(threadId);
    }
}
