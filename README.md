# Notes API

A Spring Boot REST API for managing notes.

## Running the Application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Endpoints

### Get a note
```bash
curl http://localhost:8080/api/notes/1
```

### Update a note
```bash
curl -X PUT http://localhost:8080/api/notes/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Updated Title", "content": "Updated content"}'
```

### Delete a note
```bash
curl -X DELETE http://localhost:8080/api/notes/3
```

## Sample Data

The application starts with sample notes for testing.

## Requirements

- Java 17+
- Maven 3.6+
