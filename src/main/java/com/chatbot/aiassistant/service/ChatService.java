package com.chatbot.aiassistant.service;

import com.chatbot.aiassistant.model.ChatMessage;
import com.chatbot.aiassistant.model.ChatThread;
import com.chatbot.aiassistant.model.User;
import com.chatbot.aiassistant.repository.ChatMessageRepository;
import com.chatbot.aiassistant.repository.ChatThreadRepository;
import com.chatbot.aiassistant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Δηλώνει ότι η κλάση είναι Spring Service (περιέχει επιχειρησιακή λογική)
public class ChatService {

    private final ChatThreadRepository threadRepo;        // για αποθήκευση/ανάκτηση συνομιλιών
    private final ChatMessageRepository messageRepo;      // για αποθήκευση/ανάκτηση μηνυμάτων
    private final UserRepository userRepo;                // για αναζήτηση χρηστών
    private final GroqClientService groqClient;           // για κλήση στο Groq API (AI απάντηση)

    // Constructor με όλα τα dependencies (inject μέσω Spring)
    public ChatService(ChatThreadRepository threadRepo,
                       ChatMessageRepository messageRepo,
                       UserRepository userRepo,
                       GroqClientService groqClient) {
        this.threadRepo = threadRepo;
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.groqClient = groqClient;
    }

    // === Δημιουργία νέας συνομιλίας (chat thread) με πρώτη ερώτηση ===
    public String startNewThread(Long userId, String question) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");

        // Παίρνουμε απάντηση από το AI
        String response = groqClient.getGroqResponse(question);

        // Δημιουργούμε το thread
        ChatThread thread = new ChatThread();
        thread.setTitle("Νέα Συνομιλία");
        thread.setUser(userOpt.get());

        // Μήνυμα χρήστη
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSender("user");
        userMsg.setContent(question);
        userMsg.setThread(thread);

        // Μήνυμα bot
        ChatMessage botMsg = new ChatMessage();
        botMsg.setSender("bot");
        botMsg.setContent(response);
        botMsg.setThread(thread);

        // Συνδέουμε τα μηνύματα με το thread και σώζουμε
        thread.setMessages(List.of(userMsg, botMsg));
        threadRepo.save(thread);

        return response;
    }

    // === Επιστροφή όλων των συνομιλιών του χρήστη ===
    public List<ChatThread> getThreadsByUser(Long userId) {
        return threadRepo.findByUserId(userId);
    }

    // === Επιστροφή ενός συγκεκριμένου thread με τα μηνύματά του ===
    public Optional<ChatThread> getThreadById(Long id) {
        return threadRepo.findById(id);
    }

    // === Προσθήκη ερώτησης/απάντησης σε υπάρχον thread ===
    public String appendToThread(Long userId, Long threadId, String question) {
        Optional<ChatThread> threadOpt = threadRepo.findById(threadId);
        if (threadOpt.isEmpty()) throw new RuntimeException("Thread not found");

        ChatThread thread = threadOpt.get();

        // Παίρνουμε απάντηση από το AI
        String response = groqClient.getGroqResponse(question);

        // Δημιουργούμε 2 νέα μηνύματα
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSender("user");
        userMsg.setContent(question);
        userMsg.setThread(thread);

        ChatMessage botMsg = new ChatMessage();
        botMsg.setSender("bot");
        botMsg.setContent(response);
        botMsg.setThread(thread);

        // Τα αποθηκεύουμε
        messageRepo.saveAll(List.of(userMsg, botMsg));

        return response;
    }

    // === Εναλλακτική προσθήκη ενός μόνο μηνύματος (χρήστη ή bot) ===
    public String addMessageToThread(Long threadId, String sender, String content) {
        Optional<ChatThread> threadOpt = threadRepo.findById(threadId);
        if (threadOpt.isEmpty()) throw new RuntimeException("Thread not found");

        ChatMessage msg = new ChatMessage();
        msg.setSender(sender);
        msg.setContent(content);
        msg.setThread(threadOpt.get());

        messageRepo.save(msg);

        return content;
    }

    // === Πρόσβαση στο GroqClient (αν χρειάζεται εξωτερικά) ===
    public GroqClientService getGroqClient() {
        return groqClient;
    }

    // === Διαγραφή συνομιλίας από τη βάση ===
    public void deleteThread(Long threadId) {
        threadRepo.deleteById(threadId);
    }

}
