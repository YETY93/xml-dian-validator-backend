# AGENTS.md - XML Validator DIAN

This document provides guidelines for AI agents working on this Spring Boot XML validation project.

## Build Commands

```bash
# Clean and compile
mvn clean compile

# Build JAR
mvn clean package -DskipTests

# Run application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring.profiles.active=dev
```

## Testing

```bash
# Run all tests
mvn test

# Run single test class
mvn test -Dtest=XmlValidatorApplicationTests

# Run single test method
mvn test -Dtest=XmlValidatorApplicationTests#contextLoads

# Run with verbose output
mvn test -X
```

## Code Style Guidelines

### Java Conventions
- **Java Version**: 17 (use modern features like records, sealed classes where appropriate)
- **Line Length**: 120 characters maximum
- **Indentation**: 4 spaces (no tabs)
- **Braces**: K&R style (opening brace on same line)

### Naming Conventions
- **Classes**: PascalCase (e.g., `XmlValidatorApplication`, `HealthController`)
- **Methods/Variables**: camelCase (e.g., `validateXml()`, `documentType`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_FILE_SIZE`)
- **Packages**: lowercase (e.g., `com.yesidrangel.dian.xml.validator.controller`)
- **Test Classes**: `<ClassName>Tests` suffix (e.g., `HealthControllerTests`)

### Import Organization
```java
// Standard Java imports
import java.util.List;
import java.util.Optional;

// Spring Framework imports
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// JUnit imports
import org.junit.jupiter.api.Test;

// Blank line between import groups
// Alphabetical order within each group
```

### Project Structure
```
com.yesidrangel.dian.xml.validator
├── controller        # REST Controllers (@RestController)
├── service           # Business logic interfaces
│   └── impl          # Implementations
├── domain
│   └── dto           # Request/Response DTOs
├── util              # Utility classes
├── exception         # Custom exceptions & handlers
└── XmlValidatorApplication.java
```

### Class Structure
1. Package declaration
2. Imports
3. Annotations (@RestController, @Service, etc.)
4. Class declaration
5. Constants
6. Fields
7. Constructors
8. Public methods
9. Private methods

### DTOs and Records
Prefer Java records for immutable DTOs:
```java
public record ValidationRequest(
    @NotBlank String xml,
    @NotNull DocumentType documentType
) {}
```

### Error Handling
- Use `@ControllerAdvice` for global exception handling
- Return consistent error responses with `valid: false` and `errors` array
- Log exceptions with appropriate level (ERROR for failures, WARN for business logic issues)
- Validate inputs with `@Valid` and Jakarta Validation annotations

### REST API Patterns
- Use `@RestController` for REST endpoints
- Return `ResponseEntity<T>` for flexibility in status codes
- Use proper HTTP status codes (200, 400, 500, etc.)
- Document endpoints with inline comments

### Testing
- Write unit tests for service layer using JUnit 5
- Use `@SpringBootTest` for integration tests
- Mock dependencies with Mockito
- Follow AAA pattern (Arrange, Act, Assert)

## GitFlow Workflow

| Branch | Purpose | Merge To |
|--------|---------|----------|
| `main` | Production releases | - |
| `develop` | Integration | `main` via release |
| `feature/*` | New features | `develop` |
| `release/*` | Release preparation | `main`, `develop` |
| `hotfix/*` | Urgent fixes | `main`, `develop` |

## Git Conventions

- **Commits**: Clear, imperative mood ("Add validation endpoint" not "Added validation endpoint")
- **Branches**: `feature/description`, `hotfix/issue-description`
- **PRs**: Require 1 approval for `main` and `develop`
- **Tags**: Annotated tags for releases (`v1.0.0`)

## IDE Configuration

- Project uses Maven wrapper (`mvnw`)
- Import as Maven project in IntelliJ/Eclipse
- Set Java 17 as project SDK
- Enable annotation processing for Lombok (if added)

## Additional Notes

- This is a Spring Boot 3.5.9 application
- Jakarta Validation for input validation
- Jakarta XML Validation for XSD schema validation
- Target DIAN UBL 2.1 XML documents
