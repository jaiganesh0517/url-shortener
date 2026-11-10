# URL Shortener

A clean, production-style URL Shortener built with **Spring Boot**, **PostgreSQL**, and **Redis**.

This project demonstrates real-world backend engineering practices: caching, rate limiting, proper exception handling, and clean architecture. It is suitable for portfolio and interview discussions.

---

## Features

- Create short URLs using Base62 encoding
- Fast redirection with Redis caching
- Click count tracking
- IP-based rate limiting
- Stats API
- Global exception handling with meaningful error responses
- Clean, layered architecture

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **Cache:** Redis (Memurai)
- **ORM:** Spring Data JPA
- **Build Tool:** Maven

---

## Architecture Overview

### 1. Create Short URL
1. Validate request
2. Apply rate limiting
3. Generate Base62 short code
4. Save in PostgreSQL
5. Cache in Redis

### 2. Redirection
1. Check Redis first (cache hit)
2. On miss, fetch from DB and store in Redis
3. Increment click count
4. Return `302` redirect

### 3. Rate Limiting
- Redis-based fixed window counter
- IP based
- Returns HTTP `429` when exceeded

---

## API Endpoints

- **POST** `/api/v1/shorten` : Create a short URL (no auth)
- **GET** `/{shortCode}` : Redirect to original URL (no auth)
- **GET** `/api/v1/stats/{shortCode}` : Get click statistics (no auth)

### Example: Create Short URL

**Request**

```http
POST /api/v1/shorten
Content-Type: application/json

{
  "originalUrl": "https://www.lets-code.co.in"
}
```

**Response**

```json
{
  "shortUrl": "http://localhost:8080/b",
  "shortCode": "b",
  "originalUrl": "https://www.lets-code.co.in",
  "createdAt": "2026-11-10T15:30:00"
}
```

---

## Rate Limiting

- Default: **10 requests per 60 seconds per IP**
- Configurable via `application.yml`
- Returns `429 Too Many Requests` when the limit is exceeded

---

## How to Run Locally

### Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Redis / Memurai

### Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/url-shortener.git
   cd url-shortener
   ```

2. **Create the database**

   ```sql
   CREATE DATABASE url_shortener;
   ```

3. **Update `application.yml`** with your DB password

4. **Start Redis / Memurai**

5. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

---

## Project Structure

```text
src/main/java/com/jaiganesh/urlshortener
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── UrlShortenerApplication.java
```

---

## Future Improvements

- Asynchronous click counting
- Custom short codes
- User authentication
- Analytics dashboard
- Expiration of short URLs
- Docker + CI/CD

---

## Author

**Jai Ganesh**
Aspiring Backend Engineer 