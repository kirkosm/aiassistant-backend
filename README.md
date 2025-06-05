# aiassistant-backend

Το `aiassistant-backend` αποτελεί το backend μέρος μιας full-stack εφαρμογής που προσομοιώνει λειτουργίες συνομιλίας τύπου ChatGPT. Ο χρήστης έχει τη δυνατότητα εγγραφής και σύνδεσης και μπορεί να ξεκινήσει συνομιλία με ένα AI μοντέλο, το οποίο απαντά δυναμικά μέσω Large Language Model (LLM) integration.

## Περιγραφή

Η εφαρμογή υποστηρίζει:
- Εγγραφή και είσοδο χρηστών (Signup / Login)
- Διαχείριση συνομιλιών (chat sessions)
- Αποστολή μηνυμάτων προς AI και λήψη απαντήσεων

Το backend είναι υλοποιημένο με Spring Boot και αλληλεπιδρά με βάση δεδομένων PostgreSQL.

## Τεχνολογίες

- Java 17
- Spring Boot
- Spring Security
- PostgreSQL
- Maven
- JWT Authentication
- REST API
- LLM API Integration (π.χ. Groq, OpenAI)

## Οδηγίες Εκτέλεσης

```bash
git clone https://github.com/your-username/aiassistant-backend.git
cd aiassistant-backend

Ρύθμιση Βάσης Δεδομένων
Δημιουργήστε ένα schema στη PostgreSQL και ενημερώστε το application.properties με τα δικά σας credentials:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_db
spring.datasource.username= "postgres"  
spring.datasource.password= "1234"

API Endpoints
Μέθοδος	Endpoint
POST	/api/auth/signup	Εγγραφή νέου χρήστη
POST	/api/auth/login	Είσοδος χρήστη
POST	/api/chat/message	Αποστολή μηνύματος στο AI
