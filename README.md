# User Management Service

A robust and scalable RESTful API service for managing users, built with modern Java technologies and best practices.


## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Development Setup](#local-development-setup)
  - [Docker Setup](#docker-setup)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Deployment](#deployment)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

---

## 🎯 Overview

User Management Service is a production-ready backend application that provides comprehensive user management capabilities. The service follows RESTful API design principles and implements enterprise-grade features including pagination, filtering, database migration, and containerization.

**Live Demo:** [Deployed API URL](#) *(Coming soon)*

---

## ✨ Features

### Core Functionality
- ✅ **CRUD Operations** - Create, Read, Update, Delete users
- ✅ **User Validation** - Email format, phone number validation
- ✅ **Role Management** - Support for USER, ADMIN, MODERATOR roles
- ✅ **Active Status** - Enable/disable user accounts

### Advanced Features
- 🔍 **Pagination & Sorting** - Efficient data retrieval with customizable page size and sorting
- 🎯 **Advanced Filtering** - Filter by role, active status, or combination
- 🗃️ **Database Migration** - Version-controlled schema management with Flyway
- 🐳 **Dockerized** - Fully containerized application with Docker Compose
- 📝 **Comprehensive Logging** - Structured logging for monitoring and debugging
- ⚠️ **Error Handling** - Global exception handling with meaningful error responses
- 🧪 **Unit Tests** - Service and controller layer test coverage

---

## 🛠️ Technology Stack

### Backend
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.5.7** - Enterprise-grade framework
- **Spring Data JPA** - Database abstraction and ORM
- **Hibernate 6** - Object-relational mapping
- **Maven** - Dependency management and build tool

### Database
- **PostgreSQL 15** - Robust relational database
- **Flyway** - Database version control and migration

### DevOps & Tools
- **Docker & Docker Compose** - Containerization
- **Lombok** - Reduce boilerplate code
- **SLF4J & Logback** - Logging framework

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing support

---

## Architecture

The application follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST API Endpoints
├─────────────────────────────────────┤
│          Service Layer              │  ← Business Logic
├─────────────────────────────────────┤
│         Repository Layer            │  ← Data Access
├─────────────────────────────────────┤
│        Database (PostgreSQL)        │  ← Persistence
└─────────────────────────────────────┘
```

### Design Patterns
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - Separation of internal and external data models
- **Dependency Injection** - Loose coupling and testability
- **Builder Pattern** - Lombok @Data, @Builder annotations

---

## 🚀 Getting Started

### Prerequisites

Before running the application, ensure you have:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 15** ([Download](https://www.postgresql.org/download/))
- **Docker & Docker Compose** (Optional, for containerized setup) ([Download](https://www.docker.com/))

---

### Local Development Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/shahmar-abasov/user-management.git
cd user-management
```

#### 2. Configure Database

Create a PostgreSQL database:

```sql
CREATE DATABASE userdb;
```

### 3. Configure Application Properties

The application uses default PostgreSQL credentials for local development.

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```




### 4. Build and Run

```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

### 5. Verify Installation

```bash
curl http://localhost:8080/api/v1/users/health
```

**Expected response:**
```
"User Management Service is running!"
```

---

## Docker Setup

### Run with Docker Compose (Recommended)

The easiest way to run the entire application stack:

```bash
# Build and start all services
docker-compose up --build

# Run in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

**This will start:**
- PostgreSQL database on port `5432`
- Spring Boot application on port `8080`

---

### Manual Docker Build

```bash
# Build the Docker image
docker build -t user-management-app .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/userdb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  user-management-app
```

---

## 📚 API Documentation

### Base URL

- **Local:** `http://localhost:8080/api/v1`
- **Production:** `[Deployed URL]/api/v1`

---

### Endpoints

#### Health Check

```http
GET /users/health
```

**Response:**
```json
"User Management Service is running!"
```

---

#### Create User

```http
POST /users
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+994501234567",
  "role": "USER",
  "active": true
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+994501234567",
  "role": "USER",
  "active": true,
  "createdAt": "2025-11-17T10:30:00",
  "updatedAt": "2025-11-17T10:30:00"
}
```

---

#### Get User by ID

```http
GET /users/{id}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+994501234567",
  "role": "USER",
  "active": true,
  "createdAt": "2025-11-17T10:30:00",
  "updatedAt": "2025-11-17T10:30:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "User not found with ID: 999",
  "timestamp": "2025-11-17T10:35:00"
}
```

---

#### Get All Users

```http
GET /users
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+994501234567",
    "role": "USER",
    "active": true,
    "createdAt": "2025-11-17T10:30:00",
    "updatedAt": "2025-11-17T10:30:00"
  }
]
```

---

#### Get Users with Pagination

```http
GET /users/paginated?page=0&size=10&sortBy=name&sortDirection=asc
```

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (zero-indexed) |
| `size` | integer | 10 | Number of items per page |
| `sortBy` | string | id | Field to sort by |
| `sortDirection` | string | asc | Sort direction (asc or desc) |

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "+994501234567",
      "role": "USER",
      "active": true,
      "createdAt": "2025-11-17T10:30:00",
      "updatedAt": "2025-11-17T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 50,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

---

#### Filter Users

```http
GET /users/filter?active=true&role=ADMIN&page=0&size=10
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `active` | boolean | No | Filter by active status |
| `role` | string | No | Filter by role (USER, ADMIN, MODERATOR) |
| `page` | integer | No | Page number (default: 0) |
| `size` | integer | No | Items per page (default: 10) |
| `sortBy` | string | No | Sort field (default: id) |
| `sortDirection` | string | No | Sort direction (default: asc) |

**Response:** `200 OK` (Same structure as pagination response)

---

#### Update User

```http
PUT /users/{id}
Content-Type: application/json
```

**Request Body:** (All fields are optional)
```json
{
  "name": "Jane Doe",
  "phone": "+994509876543",
  "role": "ADMIN",
  "active": false
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Jane Doe",
  "email": "john.doe@example.com",
  "phone": "+994509876543",
  "role": "ADMIN",
  "active": false,
  "createdAt": "2025-11-17T10:30:00",
  "updatedAt": "2025-11-17T11:00:00"
}
```

---

#### Delete User

```http
DELETE /users/{id}
```

**Response:** `204 No Content`

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "User not found with ID: 999",
  "timestamp": "2025-11-17T11:05:00"
}
```

---

### Error Responses

All errors follow a consistent format:

```json
{
  "status": 400,
  "message": "Validation error message",
  "timestamp": "2025-11-17T10:40:00"
}
```

**HTTP Status Codes:**

| Code | Description |
|------|-------------|
| `200 OK` | Successful GET/PUT request |
| `201 Created` | Successful POST request |
| `204 No Content` | Successful DELETE request |
| `400 Bad Request` | Validation error |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate email |
| `500 Internal Server Error` | Server error |

---

## 🗄️ Database Schema

### Users Table

| Column | Type | Constraints |
|--------|------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY |
| `name` | VARCHAR(100) | NOT NULL |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE |
| `phone` | VARCHAR(20) | - |
| `role` | VARCHAR(20) | NOT NULL, CHECK (USER/ADMIN/MODERATOR) |
| `active` | BOOLEAN | NOT NULL, DEFAULT true |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

**Indexes:**
- `idx_users_email` - Fast email lookups
- `idx_users_role` - Efficient role filtering
- `idx_users_active` - Quick active status queries

**Migrations:**
- `V1__Create_users_table.sql` - Initial schema creation

---

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Test Coverage

- ✅ **Service Layer** - Full coverage of business logic
- ✅ **Controller Layer** - API endpoint testing
- ✅ **Repository Layer** - Covered by integration tests

---

## 🚢 Deployment

### Environment Variables

Configure these environment variables for production:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/userdb
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_FLYWAY_ENABLED=true
```

### Deploy to Cloud Platforms

The application is ready to deploy on:
- **Render**
- **Railway**
- **Fly.io**
- **Heroku**
- **AWS / Azure / GCP**

**Steps:**
1. Connect your GitHub repository
2. Set environment variables
3. Deploy!

> 📖 Detailed deployment guide coming soon

---

## 📁 Project Structure

```
user-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/usermanagement/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business Logic
│   │   │   ├── repository/          # Data Access Layer
│   │   │   ├── model/               # Entity Models
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Custom Exceptions & Handlers
│   │   │   └── UserManagementApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway Migration Scripts
│   │       └── application.properties
│   └── test/
│       └── java/                    # Unit & Integration Tests
├── docker-compose.yml               # Docker Compose Configuration
├── Dockerfile                       # Docker Image Definition
├── .dockerignore                    # Docker Build Exclusions
├── .gitignore                       # Git Ignored Files
├── pom.xml                          # Maven Dependencies
└── README.md                        # This File
```

---





>