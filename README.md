# Sistema de Gestión Académica - AcademyHub

[![Java](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-%236DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2024.0.3-%236DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-%23336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-✓-%232496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![Swagger](https://img.shields.io/badge/Swagger-✓-%2385EA2D?logo=swagger&logoColor=black)](https://swagger.io/)
[![Postman](https://img.shields.io/badge/Postman-Collection-%23FF6C37?logo=postman&logoColor=white)](Postman_AcademyHub.json)
[![CI](https://img.shields.io/badge/CI-GitHub_Actions-%232088FF?logo=githubactions&logoColor=white)](.github/workflows/maven-ci.yml)
[![License](https://img.shields.io/badge/License-MIT-%23A31F34)]()

![AcademyHub](https://img.shields.io/badge/AcademyHub-Microservices-%236366F1?style=for-the-badge)

Sistema de gestión académica basado en **microservicios** con Spring Boot 3.4.5, Spring Cloud 2024.0.3 y PostgreSQL 16. Incluye autenticación JWT, Circuit Breaker, Saga Pattern y control de acceso por roles.

## Arquitectura

```
[PostgreSQL 16]                     [6 Microservicios]
  ├── bd_instructor  ←→ ms-gestion-instructor (8081)
  ├── bd_alumno      ←→ ms-gestion-alumno (8082)
  ├── bd_taller      ←→ ms-gestion-taller  (8083)
  └── bd_gateway     ←→ ms-admin-api-gateway (8080)

[Config Server]  (8888)  →  central-config/config-properties/
[Registry Server] (8761)  →  Eureka Discovery
[API Gateway]     (8080)  →  JWT + CB + Retry + RateLimiter + Swagger
```

## Características

| Característica        | Implementación |
|-----------------------|----------------|
| **JWT**               | Autenticación en dos capas (Gateway + microservicio) |
| **Roles**             | ALUMNO / INSTRUCTOR / ADMIN con control granular |
| **Circuit Breaker**   | Resilience4j en Feign + Gateway con fallback JSON |
| **Rate Limiter**      | 100 requests/segundo por ruta |
| **Retry**             | 3 reintentos ante errores 502/503 |
| **Saga Pattern**      | Inscripción con compensación automática (saga_log) |
| **DTO Validation**    | Jakarta Bean Validation con mensajes |
| **Lombok**            | Código limpio sin boilerplate |
| **Swagger UI**        | Documentación interactiva con autenticación JWT |
| **Export PDF**        | Reporte de talleres descargable |
| **Postman Collection**| Colección completa con tests para importar |
| **CI/CD**             | GitHub Actions con build + test automáticos |
| **Docker**            | Despliegue completo con docker-compose |

## Inicio rápido (Docker)

```bash
# Solo necesitas Docker Desktop
git clone https://github.com/Jonas26-hash/academyhub-microservices.git
cd academyhub-microservices
docker-compose up -d
```

Verificar: [http://localhost:8761](http://localhost:8761) (Eureka Dashboard)

## Swagger UI

Cada microservicio expone su propia documentación interactiva:

| Servicio  | Swagger UI |
|-----------|------------|
| Gateway   | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| Instructor| [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| Alumno    | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| Taller    | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |

## Postman Collection

Importa `Postman_AcademyHub.json` en Postman para tener todos los endpoints configurados con tests automáticos.

## Roles del sistema

| Rol         | Descripción |
|-------------|-------------|
| `ALUMNO`    | Consultar talleres, inscribirse/desinscribirse |
| `INSTRUCTOR`| CRUD de talleres |
| `ADMIN`     | Acceso total a todo el sistema |

**Admin por defecto:** `admin` / `admin123` (se crea automáticamente al iniciar)

## Endpoints

### Auth (públicos)
| Método | Endpoint | Body |
|--------|----------|------|
| POST | `/api/auth/register` | `{"username","password","rol"}` |
| POST | `/api/auth/login` | `{"username","password"}` |

### Instructores (autenticado)
| Método | Endpoint |
|--------|----------|
| GET | `/api/instructores` |
| GET | `/api/instructores/{id}` |
| POST | `/api/instructores` |
| PUT | `/api/instructores/{id}` |
| DELETE | `/api/instructores/{id}` |

### Alumnos (autenticado)
| Método | Endpoint |
|--------|----------|
| GET | `/api/alumnos` |
| GET | `/api/alumnos/{id}` |
| POST | `/api/alumnos` |
| PUT | `/api/alumnos/{id}` |
| DELETE | `/api/alumnos/{id}` |

### Talleres
| Método | Endpoint | Roles |
|--------|----------|-------|
| GET | `/api/talleres` | Autenticado |
| GET | `/api/talleres/{id}` | Autenticado |
| POST | `/api/talleres` | INSTRUCTOR, ADMIN |
| PUT | `/api/talleres/{id}` | INSTRUCTOR, ADMIN |
| DELETE | `/api/talleres/{id}` | INSTRUCTOR, ADMIN |
| POST | `/api/talleres/{id}/alumnos/{alumnoId}` | ALUMNO, ADMIN |
| DELETE | `/api/talleres/{id}/alumnos/{alumnoId}` | ALUMNO, ADMIN |
| GET | `/api/talleres/{id}/instructor` | Autenticado |
| GET | `/api/talleres/{id}/alumnos` | Autenticado |
| GET | `/api/talleres/alumno/{alumnoId}` | Autenticado |
| GET | `/api/talleres/export/pdf` | Autenticado |

## Documentación completa

Visita la [documentación en Mintlify](https://academyhub-documentation.mintlify.app) para guías detalladas, diagramas de arquitectura y referencia completa de la API.


