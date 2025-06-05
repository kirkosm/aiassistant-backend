package com.chatbot.aiassistant.service;

// Import απαραίτητων Spring component classes και HTTP εργαλείων
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

// Η κλάση δηλώνεται ως Service — ανήκει στο "business logic layer"
@Service
public class GroqClientService {

    // Το API Key του Groq αποθηκεύεται στο application.properties (.env)
    @Value("${groq.api.key}")
    private String apiKey;

    // RestTemplate είναι το εργαλείο της Spring για αποστολή HTTP requests
    private final RestTemplate restTemplate = new RestTemplate();

    // Κύρια μέθοδος που στέλνει το μήνυμα στο Groq API και επιστρέφει απάντηση
    public String getGroqResponse(String userMessage) {

        // URL του Groq API endpoint
        String url = "https://api.groq.com/openai/v1/chat/completions";

        // Δημιουργούμε το JSON body του αιτήματος
        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-8b-8192"); // ή "llama3-70b-8192" για ισχυρότερο μοντέλο

        // Το μήνυμα του χρήστη περνάει σε λίστα ως single message με ρόλο "user"
        body.put("messages", List.of(
                Map.of("role", "user", "content", userMessage)
        ));

        // Ετοιμάζουμε τα headers: τύπος και authentication
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // Το API key μπαίνει ως Bearer token

        // Πακέτο που περιλαμβάνει headers και body
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // Εκτελούμε το POST request και περιμένουμε JSON απάντηση (ως Map)
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            // Παίρνουμε από το JSON το πρώτο "choice" και μέσα εκεί την "message"
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            // Επιστρέφουμε το content της απάντησης
            return message.get("content").toString().trim();

        } catch (Exception e) {
            // Αν υπάρξει πρόβλημα (π.χ. δικτύου ή κλειδιού), επιστρέφουμε μήνυμα σφάλματος
            e.printStackTrace();
            return "⚠️ Δεν μπόρεσα να απαντήσω λόγω σφάλματος στο Groq.";
        }
    }
}
