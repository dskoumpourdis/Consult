# Customer Consultation Survey Application

## Table of Contents

* [Overview](#overview)
* [Features](#features)
* [Technology Stack](#technology-stack)
* [Getting Started](#getting-started)
* [API Endpoints](#api-endpoints)
* [Testing](#testing)
* [Notes](#notes)

---

## Overview

This application allows the user to create **consultations (questionnaires/surveys)** with multiple questions and choices, collect user answers, and compute likelihood scores based on numeric responses.

It supports **REST API** interactions.

---

## Features

* Create, read, and manage consultations and questions
* Support for multiple question types: multiple-choice, text, numeric
* Add choices to multiple-choice questions
* Submit answers to a consultation
* Automatic likelihood calculation (Not Likely / Likely / Very Likely)
* JSON-based REST API
* Unit and integration test coverage

---

## Technology Stack

* Java 17+
* Spring Boot

    * Spring Data JPA (Hibernate)
    * Spring Web
    * Spring Validation
* H2 Database (in-memory for testing, can switch to MySQL/PostgreSQL)
* JUnit 5, Mockito for testing
* Maven for build and dependency management
* Jackson for JSON serialization

---

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.8+

### Running the Application

1. Clone the repository:

```bash
git clone https://github.com/yourusername/consultation-survey.git
cd consultation-survey
```

2. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

3. Access the application:

* API: `http://localhost:8080/api/consultations`

---

## API Endpoints

### Get All Consultations

```
GET /api/consultations
```

**Response Example:**

```json
[
  {
    "id": 1,
    "title": "Customer Satisfaction Survey",
    "questions": [
      {
        "id": 1,
        "text": "How would you rate our service?",
        "type": "MULTIPLE_CHOICE",
        "choices": [
          { "id": 1, "choice": "1" },
          { "id": 2, "choice": "2" }
        ]
      }
    ]
  }
]
```

---

### Create Consultation

```
POST /api/consultations
```

**Request Body:**

```json
{
  "title": "New Survey",
  "questions": [
    {
      "text": "How satisfied are you?",
      "type": "MULTIPLE_CHOICE",
      "choices": [
        { "choice": "1" },
        { "choice": "2" }
      ]
    }
  ]
}
```

**Response:** `201 Created` with saved consultation JSON.

---

### Submit Answers

```
POST /api/consultations/{id}/answers
```

**Request Body:**

```json
[
  { "response": 2, "question": { "id": 1 }, "userId": "user123" },
  { "response": 15, "question": { "id": 2 }, "userId": "user123" }
]
```

**Response:**

```
It is not likely that the prescription will be approved
```

* Scoring logic: sum of numeric answers determines likelihood:

    * 0-30 - Not Likely
    * 31-60 - Likely
    *  60 - Very Likely

---


## Testing

* **Unit Tests:** Service layer (`ConsultationServiceTest`) using Mockito
* **Integration Tests:** REST endpoints (`ConsultationControllerIntegrationTest`) using MockMvc
* **Test coverage includes:**

    * Consultation creation
    * Question and choice persistence
    * Submitting answers with all likelihood ranges
    * JSON serialization for nested objects

Run tests:

```bash
mvn test
```

---

## Notes

**Functionality that could be implemented in a business scenario:**

    Support scoring for all kinds of question types
    Front-end implementation
    Permanent storage
    Use transfer resources to decouple the api from the persistence models

