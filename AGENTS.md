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

## Code Formatting

```bash
# Check code formatting (fails if not formatted)
mvn spotless:check

# Apply code formatting automatically
mvn spotless:apply

# Format and check in one command
mvn spotless:apply && mvn spotless:check
```

**Note**: Spotless runs automatically during `mvn clean install` (verify phase)

## Code Style Guidelines

### Java Conventions
- **Java Version**: 17 (use modern features like records, sealed classes where appropriate)
- **Line Length**: 120 characters maximum
- **Indentation**: 4 spaces (no tabs)
- **Braces**: K&R style (opening brace on same line)

### Naming Conventions
- **Classes**: PascalCase (e.g., `XmlValidatorApplication`, `HealthController`)
- **Methods/Variables**: camelCase (e.g., `validateXml()`, `documentType`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `XSD_NO_ENCONTRADO`)
- **Packages**: lowercase (e.g., `com.yesidrangel.dian.xml.validator.controller`)
- **Test Classes**: `<ClassName>Tests` suffix (e.g., `HealthControllerTests`)
- **Enums**: PascalCase singular (e.g., `DianSchemaType`, `ResponseCodeEnum`)

### Import Organization
```java
// Standard Java imports (java.*)
import java.io.InputStream;
import java.util.List;

// Third-party imports (org.*, com.*)
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// Blank line between import groups
// Alphabetical order within each group
// No wildcard imports (avoid import java.util.*)
```

### Lombok Usage
Use Lombok annotations to reduce boilerplate:
- `@Data` for DTOs with getters/setters/equals/hashCode/toString
- `@Builder` for DTOs with builder pattern
- `@Slf4j` for logging (preferred over manual logger creation)
- `@Getter/@Setter` for selective accessor generation
- `@AllArgsConstructor/@NoArgsConstructor` as needed

**Lombok Configuration**:
- Version: 1.18.30 (compile), 1.18.38 (annotation processor)
- Scope: `provided` (not included in JAR)
- Annotation processing enabled in Maven compiler plugin
- Requires IDE annotation processing enabled

### Project Structure
```
com.yesidrangel.dian.xml.validator
├── controller        # REST Controllers (@RestController)
├── service           # Business logic interfaces
│   └── impl          # Implementations
├── domain
│   ├── dto           # Request/Response DTOs (@Data, @Builder)
│   └── enums         # Enumerations with Optional lookup methods
├── infrastructure    # Cross-cutting concerns (factories)
├── util              # Static utility classes
├── exception         # Custom exceptions & handlers
└── XmlValidatorApplication.java
```

### Class Structure
1. Package declaration
2. Imports (organized as above)
3. Class-level annotations (@RestController, @Service, @Slf4j, etc.)
4. Class declaration
5. Static constants (UPPER_SNAKE_CASE)
6. Instance fields (private final injected dependencies first)
7. Constructor (dependency injection)
8. Public methods
9. Private methods

### Service Layer Pattern
- Define interfaces in `service` package
- Implement in `service.impl` package with `@Service`
- Use constructor injection exclusively (no @Autowired on fields)
- Add `@Slf4j` for logging business operations

### DTOs and Records
Prefer Lombok `@Data` + `@Builder` for mutable DTOs:
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XmlValidationRequestDto {
    private String xml;
    private String documentType;
}
```

Use Java records for immutable DTOs when possible:
```java
public record ValidationRequest(
    @NotBlank String xml,
    @NotNull DocumentType documentType
) {}
```

### Error Handling
- Extend `RuntimeException` for custom exceptions
- Use `TechnicalException` for system errors (HTTP 500)
- Use `FunctionalException` for business logic errors (HTTP 400)
- Handle globally with `@RestControllerAdvice` and `@ExceptionHandler`
- Return `ApiResponseDto<T>` with consistent structure (success, status, code, data)

### REST API Patterns
- Use `@RestController` with `@RequestMapping` for base paths
- Use specific mapping annotations (`@PostMapping`, `@GetMapping`)
- Return `ResponseEntity<ApiResponseDto<T>>` for flexibility
- Use `@RequestBody` with DTOs for request bodies
- Use `@Valid` with Jakarta Validation annotations for input validation
- Document endpoints with inline comments

### Logging
- Use `@Slf4j` annotation (lombok.extern.slf4j.Slf4j)
- Levels: INFO for business operations, WARN for validation issues, ERROR for failures
- Log meaningful context: `log.info("Validando documento {}", requestDto.getDocumentType())`

### Utility Classes
- Use `public final class` with private constructor
- Define constants as `public static final`
- Use `static` methods for stateless operations
- Example: `XsdValidationUtil.validate(xml, xsdPath)`

### Enum Patterns
- Add static lookup methods returning `Optional<T>`
- Use uppercase enum values with underscores
- Include descriptive fields (code, xsdPath, etc.)
- Example: `DianSchemaType.forName("INVOICE")`

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
- Use `--no-ff` for merges to preserve history

## IDE Configuration

- Project uses Maven wrapper (`mvnw`)
- Import as Maven project in IntelliJ/Eclipse
- Set Java 17 as project SDK
- Enable annotation processing for Lombok

## Git Configuration

### GitIgnore Patterns
- Build artifacts: `target/`, `*.class`, `*.jar`
- IDE files: `.idea/`, `.settings/`, `.project`, `.classpath`
- Logs: `*.log`, `logs/`
- Local files: `WARP.md`, `HELP.mdtodos`, `HELP.md`
- Application config: `application-local.*`

### Git Workflow
- **Never push** without explicit authorization
- Always run tests before committing
- Use `git status` to review changes before staging

## Additional Notes

- Spring Boot 3.5.9 application
- Jakarta Validation for input validation
- Jakarta XML Validation for XSD schema validation
- Target DIAN UBL 2.1 XML documents
- Factory pattern for API responses: `ApiResponseFactory.success()`, `ApiResponseFactory.error()`
- XSD schemas located in `src/main/resources/xsd/` (factura, documento-soporte, nomina)

## Project Statistics

**Current State**:
- 17 Java source files (1 test, 16 main)
- 3 REST endpoints (health, xml validation)
- 4 XSD schema types supported (INVOICE, CREDIT_NOTE, DOCUMENTO_SOPORTE)
- Spotless configured for automatic code formatting

**Packages Breakdown**:
- controller: 2 classes
- service: 1 interface + 1 implementation
- domain.dto: 3 DTOs
- domain.enums: 3 enums
- exception: 3 exceptions + 1 global handler
- infrastructure: 1 factory
- util: 1 utility class

## Known Issues

- Lombok annotation processing may not work in all IDEs without proper configuration
- Ensure annotation processing is enabled in IDE settings
- Run `mvn clean compile` if Lombok-generated methods are not recognized
