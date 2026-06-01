# Sistema de Gestión Académica - Microservicios

Sistema de gestión de instructores, alumnos y talleres con Spring Boot 3.4.5, Spring Cloud 2024.0.3 y PostgreSQL.

## Arquitectura

```
[PostgreSQL 16]
  ├── bd_instructor  → ms-gestion-instructor (8081)
  ├── bd_alumno      → ms-gestion-alumno (8082)
  ├── bd_taller      → ms-gestion-taller (8083)
  └── bd_gateway     → ms-admin-api-gateway (8080)

[Config Server]  (8888)  →  central-config/config-properties/
[Registry Server] (8761)  →  Eureka
[API Gateway]     (8080)  →  JWT + Circuit Breaker + Retry + Rate Limiter
```

## Requisitos

- Java 21
- Docker Desktop (para PostgreSQL)
- Git (para clonar)

## Inicio rápido

### Windows
```bash
start.bat
```

### Linux / Mac
```bash
chmod +x start.sh
./start.sh
```

### Manual (abrir terminales separadas)
```bash
# Terminal 1: PostgreSQL
docker-compose up -d

# Terminal 2: Config Server
cd ms-admin-config-server && ./mvnw spring-boot:run

# Terminal 3: Registry Server
cd ms-admin-registry-server && ./mvnw spring-boot:run

# Terminal 4: API Gateway
cd ms-admin-api-gateway && ./mvnw spring-boot:run

# Terminal 5: Instructor
cd ms-gestion-instructor && ./mvnw spring-boot:run

# Terminal 6: Alumno
cd ms-gestion-alumno && ./mvnw spring-boot:run

# Terminal 7: Taller
cd ms-gestion-taller && ./mvnw spring-boot:run
```

Verificar: http://localhost:8761 (Eureka Dashboard)

## Endpoints (Gateway :8080)

### Auth
| Método | Endpoint | Body |
|--------|----------|------|
| POST | `/api/auth/register` | `{"username":"x","password":"y"}` |
| POST | `/api/auth/login` | `{"username":"x","password":"y"}` |

### Instructor
| Método | Endpoint |
|--------|----------|
| GET, POST | `/api/instructores` |
| GET, PUT, DELETE | `/api/instructores/{id}` |

### Alumno
| Método | Endpoint |
|--------|----------|
| GET, POST | `/api/alumnos` |
| GET, PUT, DELETE | `/api/alumnos/{id}` |

### Taller
| Método | Endpoint |
|--------|----------|
| GET, POST | `/api/talleres` |
| GET, PUT, DELETE | `/api/talleres/{id}` |
| POST | `/api/talleres/{id}/inscribir/{alumnoId}` |
| POST | `/api/talleres/{id}/desinscribir/{alumnoId}` |
| GET | `/api/talleres/{id}/instructor` |
| GET | `/api/talleres/{id}/alumnos` |

> Todos los endpoints requieren `Authorization: Bearer <token>` (excepto register/login)

## Características

- **JWT**: Autenticación en Gateway + cada microservicio
- **Circuit Breaker**: Resilience4j en Feign clients + Gateway
- **Rate Limiter**: 100 requests/segundo por Gateway
- **Retry**: 3 reintentos automáticos en Gateway
- **Audit Log**: Cada request logueado en consola del Gateway
- **Saga Pattern**: Inscripción de alumnos con compensación automática
