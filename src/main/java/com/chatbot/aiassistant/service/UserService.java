package com.chatbot.aiassistant.service;

// Εισάγουμε το μοντέλο User που αντιστοιχεί σε έναν χρήστη της εφαρμογής
import com.chatbot.aiassistant.model.User;
// Εισάγουμε το repository που μας επιτρέπει να εκτελούμε CRUD εντολές στη βάση
import com.chatbot.aiassistant.repository.UserRepository;
// Εισάγουμε annotations και κρυπτογράφηση από Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Δηλώνουμε ότι αυτή η κλάση είναι Service (λογική επιχειρηματικού επιπέδου)
@Service
public class UserService {

    // Χρησιμοποιούμε το repository για αλληλεπίδραση με τη βάση
    private final UserRepository userRepository;

    // Χρησιμοποιούμε το BCrypt για ασφαλή αποθήκευση/σύγκριση κωδικών
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor με dependency injection
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

        // Αρχικοποιούμε τον encoder (μπορεί να μπει και σαν @Bean αλλού)
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // === Εγγραφή νέου χρήστη ===
    public String registerUser(String username, String rawPassword, String email) {

        // Αν υπάρχει ήδη χρήστης με αυτό το username, επιστρέφουμε μήνυμα
        if (userRepository.existsByUsername(username)) {
            return "Username is already taken.";
        }

        // Αν υπάρχει ήδη χρήστης με αυτό το email, επιστρέφουμε μήνυμα
        if (userRepository.existsByEmail(email)) {
            return "Email is already in use.";
        }

        // Κρυπτογράφηση του κωδικού πριν αποθήκευση
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Δημιουργία νέου χρήστη και αποθήκευση στη βάση
        User user = new User(username, encodedPassword, email);
        userRepository.save(user);

        // Επιστρέφουμε μήνυμα επιτυχίας
        return "User registered successfully.";
    }

    // === Είσοδος χρήστη (επιστρέφει απλώς μήνυμα) ===
    public String loginUser(String username, String rawPassword) {

        // Ψάχνουμε αν υπάρχει χρήστης με αυτό το username
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Αν δεν υπάρχει, αποτυχημένη σύνδεση
        if (userOptional.isEmpty()) {
            return "Invalid username or password.";
        }

        // Παίρνουμε τον χρήστη
        User user = userOptional.get();

        // Συγκρίνουμε τον raw password με τον κρυπτογραφημένο στη βάση
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return "Invalid username or password.";
        }

        // Αν όλα είναι εντάξει, επιστρέφουμε επιτυχία
        return "Login successful.";
    }

    // === Εναλλακτική σύνδεση: επιστρέφει το αντικείμενο User ===
    public User authenticate(String username, String rawPassword) {
        // Ψάχνουμε αν υπάρχει χρήστης
        Optional<User> userOpt = userRepository.findByUsername(username);

        // Αν βρέθηκε και ταιριάζει ο κωδικός...
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user; // Επιστροφή του χρήστη
            }
        }

        // Αν κάτι πήγε λάθος, επιστροφή null
        return null;
    }
}
