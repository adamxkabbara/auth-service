# Auth Service

This service provides REST API endpoints for user registration and authentication using JWT (JSON Web Tokens). It is built with Spring Boot and uses H2 as an in-memory database.

## Features

-   Register new users with username, email, password, first name, and last name
-   Authenticate users and issue JWT access tokens
-   Passwords are securely hashed using BCrypt
-   JWT-based stateless authentication
-   Basic error handling for authentication and registration
-   In-memory H2 database for development and testing

## Technologies Used

-   Java 17+ (configured for Java 24)
-   Spring Boot 3.x
-   Spring Security
-   Spring Data JPA
-   H2 Database
-   JWT (io.jsonwebtoken)
-   Lombok

## Getting Started

### Prerequisites

-   Java Development Kit (JDK) 17 or higher
-   Maven

### Installation

1. Clone the repository:

    ```sh
    git clone <repository_url>
    cd auth-service
    ```

2. Set environment variables for database and JWT secret key. You can use a `.env` file or set them in your environment:

    ```
    DATABASE_USERNAME=your_db_username
    DATABASE_PASSWORD=your_db_password
    JWT_SECRET_KEY=your_jwt_secret
    ```

3. Build and run the application:
    ```sh
    mvn spring-boot:run
    ```

### API Endpoints

#### Register User

-   **POST** `/api/auth/register`
-   **Request Body:**
    ```json
    {
        "username": "johndoe",
        "email": "john@example.com",
        "password": "yourpassword",
        "firstname": "John",
        "lastname": "Doe"
    }
    ```
-   **Response:** `200 OK` on success

#### Authenticate User

-   **POST** `/api/auth/authenticate`
-   **Request Body:**
    ```json
    {
        "username": "johndoe",
        "password": "yourpassword"
    }
    ```
-   **Response:**
    ```json
    {
        "accessToken": "jwt_token_here",
        "expiresIn": 900000
    }
    ```

### H2 Database Console

-   Accessible at: `http://localhost:8080/h2-console`
-   JDBC URL: `jdbc:h2:mem:mydb`
-   Username/Password: as set in your environment variables

## Project Structure

-   `src/main/java/com/growtivat/auth_service/` - Main source code
-   `src/main/resources/application.properties` - Application configuration
-   `src/test/java/com/growtivat/auth_service/` - Test classes

## License

This project is licensed under the MIT License.

---

**Note:** This service is intended for development and educational purposes. For production use, ensure to secure your secret keys and review security configurations.
