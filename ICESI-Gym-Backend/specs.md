# Project Specifications Manifest (SDD)

This document serves as the central inventory for all project specifications. Following **Spec Driven Development (SDD)** practices, this manifest tracks the definition, implementation, and verification status of each project module.

## 1. Data Model Specifications
Defines the core entities, relationships, and validation rules.

| Feature | Spec Location | Implementation Status | Verification Status |
| :--- | :--- | :--- | :--- |
| User Management | `entity/User.java` | Implemented | Verified (Unit/Int) |
| Role & Permissions | `entity/Role.java`, `UserRole.java` | Implemented | Verified (Unit/Int) |
| Policy Management | `entity/Policy.java` | Implemented | Verified (Unit/Int) |
| Training/Routine | `entity/Routine.java`, `Exercise.java` | Implemented | Verified (Unit/Int) |
| Enrollment/Progress| `entity/Enrollment.java`, `Progress.java` | Implemented | Verified (Unit/Int) |
| Communication | `entity/Message.java`, `Notification.java` | Implemented | Verified (Unit/Int) |
| Logistics | `entity/Space.java`, `Schedule.java` | Implemented | Verified (Unit/Int) |

## 2. API & Interface Specifications
Defines how external systems and users interact with the application.

### 2.1 REST API (v1)
All REST endpoints are documented via Swagger at `/swagger-ui.html`. They follow the standard structure: `GET /{id}`, `GET /`, `POST /`, `PUT /{id}`, `DELETE /{id}`.

| Endpoint Group | REST Controller | DTOs | Status |
| :--- | :--- | :--- | :--- |
| Authentication | `AuthRestController.java` | `AuthRequest`, `RegisterRequest` | Implemented |
| Users | `UserRestController.java` | `UserRequest`, `UserResponse` | Implemented |
| Roles | `RoleRestController.java` | `RoleRequest`, `RoleResponse` | Implemented |
| Policies | `PolicyRestController.java` | `PolicyRequest`, `PolicyResponse` | Implemented |
| Activities | `ActivityRestController.java` | `ActivityRequest`, `ActivityResponse` | Implemented |
| Enrollments | `EnrollmentRestController.java` | `EnrollmentRequest`, `EnrollmentResponse` | Implemented |
| Assignments | `AssignmentRestController.java` | `AssignmentRequest`, `AssignmentResponse` | Implemented |
| Routines | `RoutineRestController.java` | `RoutineRequest`, `RoutineResponse` | Implemented |
| Exercises | `ExerciseRestController.java` | `ExerciseRequest`, `ExerciseResponse` | Implemented |
| Progress | `ProgressRestController.java` | `ProgressRequest`, `ProgressResponse` | Implemented |
| Recommendations | `RecommendationRestController.java` | `RecommendationRequest`, `RecommendationResponse` | Implemented |
| Messages | `MessageRestController.java` | `MessageRequest`, `MessageResponse` | Implemented |
| Notifications | `NotificationRestController.java` | `NotificationRequest`, `NotificationResponse` | Implemented |
| Schedules | `ScheduleRestController.java` | `ScheduleRequest`, `ScheduleResponse` | Implemented |
| Spaces | `SpaceRestController.java` | `SpaceRequest`, `SpaceResponse` | Implemented |
| **User-Roles** | `UserRoleRestController.java` | `UserRoleRequest`, `UserRoleResponse` | Implemented |
| **Role-Policies** | `RolePolicyRestController.java` | `RolePolicyRequest`, `RolePolicyResponse` | Implemented |
| **Routine-Exercises**| `RoutineExerciseRestController.java`| `RoutineExerciseRequest`, `RoutineExerciseResponse`| Implemented |

### 2.3 Real-time Interface (WebSockets)
STOMP-based WebSocket interface for real-time notifications and messaging.

| Feature | Endpoint | Destination | Broker Type | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Connection Handshake** | `/ws` | - | SockJS | ✅ Implemented |
| **Broadcast Notifications**| - | `/topic/notifications` | Simple Broker | ✅ Implemented |
| **Private Notifications** | - | `/user/queue/notifications` | Simple Broker | ✅ Implemented |
| **Private Messages** | - | `/user/queue/messages` | Simple Broker | ✅ Implemented |

## 2.4 Web Interface (MVC)
Traditional web interface using Thymeleaf templates.

| Endpoint Group | Controller | Spec Type | Status |
| :--- | :--- | :--- | :--- |
| User Admin | `UserController.java` | MVC / Thymeleaf | Implemented |
| Role Management | `RoleController.java` | MVC / Thymeleaf | Implemented |
| Policy Admin | `PolicyController.java` | MVC / Thymeleaf | Implemented |
| Swagger/OpenAPI | Integrated | OpenAPI 3.0 | Auto-generated |

## 3. Security Specifications
Defines access control, authentication mechanisms, and authorization levels.

| Specification | Location | Mechanism | Status |
| :--- | :--- | :--- | :--- |
| Authentication | `security/WebSecurityConfig.java` | JWT / Spring Security | Implemented |
| **WebSocket Security** | `WebSocketAuthenticationInterceptor.java` | STOMP CONNECT Handshake JWT | ✅ Implemented |
| Authorization | `@PreAuthorize` in Controllers | Role-Based Access (RBAC) | Implemented |
| Password Policy | `entity/User.java` | Pattern Validation | Implemented |

## 4. Infrastructure Specifications
Defines the environment and deployment requirements.

| Component | File | Type | Status |
| :--- | :--- | :--- | :--- |
| Database (Dev/Prod) | `compose.yaml` | PostgreSQL | Implemented |
| Build System | `pom.xml` | Maven | Implemented |
| CI/CD Ready | `deploy/` directory | WAR Packaging | Implemented |

## 5. Behavior & Verification Specifications
Executable specifications (Tests) that verify the system meets the requirements.

| Test Suite | Location | Count | Status |
| :--- | :--- | :--- | :--- |
| Unit Tests | `src/test/java/.../unit` | ~160 | ✅ Verified |
| Integration Tests | `src/test/java/.../integration` | ~170 | ✅ Verified |
| Coverage Reports | `htmlReport/index.html` | Jacoco | Active |

---
**Last Updated:** May 23, 2026
**Status Legend:**
- 📝 **Draft**: Definition only.
- 🛠 **In Progress**: Coding started.
- ✅ **Implemented**: Code complete.
- 🧪 **Verified**: Tests passing.
