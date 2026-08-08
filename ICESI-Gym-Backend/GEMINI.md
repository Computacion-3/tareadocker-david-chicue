# Project Overview: proyectoFinal

This is a Spring Boot 3.5.11 application developed in Java 17. The project appears to be a management system for a fitness or educational environment, featuring entities such as `Exercise`, `Routine`, `Schedule`, `Space`, `User`, and `Enrollment`.

## Technical Stack
- **Framework:** Spring Boot 3.5.11
- **Language:** Java 17
- **Build Tool:** Maven
- **Persistence:** Spring Data JPA with PostgreSQL (production/dev) and H2 (testing)
- **Security:** Spring Security with JWT authentication
- **Mapping:** MapStruct for DTO-Entity conversions
- **Boilerplate:** Lombok
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **Templating:** Thymeleaf
- **Testing:** JUnit 5 with Jacoco for code coverage

## Project Structure
- `src/main/java/edu/co/icesi/proyectofinal/`: Root package
    - `api/`: API-related components and mappers
    - `controller/`: REST Controllers
    - `entity/`: JPA Entities and composite keys
    - `repository/`: Spring Data JPA Repositories
    - `security/`: Security configuration and JWT handling
    - `services/`: Business logic interfaces and implementations (`impl/`)
- `src/main/resources/`: Configuration files and static assets
- `src/test/java/edu/co/icesi/proyectofinal/`:
    - `unit/`: Unit tests for services
    - `integration/`: Integration tests for services and controllers

## Building and Running

### Build the project
```bash
./mvnw clean install
```

### Run the application
```bash
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

### Build Coverage Report
The project is configured with Jacoco. After running tests, the report is automatically copied to the `htmlReport/` directory in the root of the project.

## Development Conventions
- **Layered Architecture:** Adhere to the Controller -> Service -> Repository -> Entity flow.
- **DTO Mapping:** Use MapStruct for mapping between Entities and DTOs.
- **Lombok:** Use Lombok annotations (`@Data`, `@NoArgsConstructor`, etc.) to reduce boilerplate.
- **Testing:** New features should include both unit tests (in `src/test/java/.../unit`) and integration tests (in `src/test/java/.../integration`).
- **Security:** Use JWT for authentication. Security configurations are located in the `security/` package.
- **Deployment:** A WAR file is generated and copied to the `deploy/` directory upon packaging.
