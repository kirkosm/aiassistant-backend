# aiassistant-backend

aiassistant-backend is the backend service of a full-stack AI chat application inspired by ChatGPT. It handles user authentication, chat session management, and communication with an AI model using Large Language Model (LLM) integration.

## Features

- User registration and login with JWT authentication
- Chat session management
- Sending messages to an AI model and receiving responses
- Secure REST API using Spring Security
- PostgreSQL database integration

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- PostgreSQL
- JWT Authentication
- REST API
- Maven
- LLM API Integration (Groq)

## Getting Started

1. Clone the repository

2. Set up PostgreSQL database:

   - Create a database
   - Open src/main/resources/application.properties
   - Replace the database credentials as needed:

   spring.datasource.url=jdbc:postgresql://localhost:5432/aiassistant_db@localhost
   spring.datasource.username=postgres
   spring.datasource.password=1234

## API Endpoints

POST   /api/auth/signup      -> Register a new user  
POST   /api/auth/login       -> Login with user credentials  
POST   /api/chat/message     -> Send a message to the AI and receive a response

## Project Structure

src/
├── config/        - Security configuration
├── controller/    - REST controllers (authentication and chat)
├── model/         - Entity classes and data transfer objects
├── repository/    - JPA repositories for database interaction
├── service/       - Business logic and AI communication
└── resources/     - application.properties and other resources

## Notes

- You can connect the service to any LLM provider (such as Groq or OpenAI) via their API.
- For testing, use tools like Postman or Thunder Client to send HTTP requests.
