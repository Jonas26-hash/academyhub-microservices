# Sistema de Gestión Académica - Microservicios

Sistema de gestión de instructores, alumnos y talleres con Spring Boot 3.4.5, Spring Cloud 2024.0.3 y PostgreSQL 16.

## Arquitectura

```
[PostgreSQL 16]                     [6 Microservicios]
  ├── bd_instructor  ←→ ms-gestion-instructor (8081)
  ├── bd_alumno      ←→ ms-gestion-alumno (8082)
  ├── bd_taller      ←→ ms-gestion-taller (8083)
  └── bd_gateway     ←→ ms-admin-api-gateway (8080)

[Config Server]  (8888)  →  central-config/config-properties/
[Registry Server] (8761)  →  Eureka Discovery
[API Gateway]     (8080)  →  JWT + CB + Retry + RateLimiter + Audit
```

## Inicio rápido (Docker)

**Solo necesitas Docker Desktop** — no necesitas Java ni Maven instalados.

```bash
# Clonar y ejecutar
git clone <repo>
cd Jonas_EXAM
docker-compose up -d
```

Esto arranca PostgreSQL + los 6 microservicios automáticamente.

Verificar: http://localhost:8761 (Eureka — todos los servicios deben estar UP)

Esperar ~30s después del primer `up` para que el Config Server cargue las propiedades.

## Inicio rápido (local — sin Docker)

Requiere Java 21 + Maven 3.9+ + Docker (solo PostgreSQL).

```bash
# Terminal 1: BD
docker-compose up -d postgres

# Terminal 2-7 (en orden):
cd ms-admin-config-server  && mvn spring-boot:run
cd ms-admin-registry-server && mvn spring-boot:run
cd ms-admin-api-gateway     && mvn spring-boot:run
cd ms-gestion-instructor    && mvn spring-boot:run
cd ms-gestion-alumno        && mvn spring-boot:run
cd ms-gestion-taller        && mvn spring-boot:run
```

## Roles del sistema

| Rol | Descripción |
|-----|-------------|
| `ALUMNO` | Puede consultar talleres e inscribirse/desinscribirse |
| `INSTRUCTOR` | Puede crear, editar y eliminar talleres |
| `ADMIN` | Acceso total a todo el sistema |

**Admin por defecto:** `admin` / `admin123` (se crea automáticamente al iniciar)

## Endpoints (Gateway :8080)

### Auth
| Método | Endpoint | Body |
|--------|----------|------|
| POST | `/api/auth/register` | `{"username":"x","password":"y","rol":"ALUMNO"}` |
| POST | `/api/auth/login` | `{"username":"x","password":"y"}` |

Roles válidos: `ALUMNO`, `INSTRUCTOR`, `ADMIN` (default: `ALUMNO`)

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
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/talleres` | Listar todos |
| GET | `/api/talleres/{id}` | Obtener por ID |
| POST | `/api/talleres` | Crear (INSTRUCTOR/ADMIN) |
| PUT | `/api/talleres/{id}` | Actualizar (INSTRUCTOR/ADMIN) |
| DELETE | `/api/talleres/{id}` | Eliminar (INSTRUCTOR/ADMIN) |
| POST | `/api/talleres/{id}/inscribir/{alumnoId}` | Inscribir alumno (ALUMNO/ADMIN) — valida cupo |
| POST | `/api/talleres/{id}/desinscribir/{alumnoId}` | Desinscribir alumno (ALUMNO/ADMIN) |
| GET | `/api/talleres/{id}/instructor` | Datos completos del instructor |
| GET | `/api/talleres/{id}/alumnos` | Datos completos de los alumnos |
| GET | `/api/talleres/alumno/{alumnoId}` | Talleres de un alumno |

> Todos los endpoints requieren `Authorization: Bearer <token>` (excepto register/login)

## Características técnicas

| Característica | Implementación |
|--------|---------------|
| **JWT** | Autenticación en Gateway + cada microservicio |
| **Roles** | ALUMNO / INSTRUCTOR / ADMIN con control por endpoint |
| **Circuit Breaker** | Resilience4j en Feign clients + Gateway (fallback a JSON amigable) |
| **Rate Limiter** | 100 requests/segundo por Gateway |
| **Retry** | 3 reintentos automáticos ante errores 502/503 |
| **Audit Log** | Cada request logueado con método, path, status, tiempo e IP |
| **Saga Pattern** | Inscripción de alumnos con compensación automática (saga_log) |
| **Cupo** | Los talleres tienen cupo máximo; se valida al inscribir |
| **Token Propagation** | Feign propaga Bearer token entre microservicios |

## Secuencia de prueba (Postman)

```
1. POST /api/auth/login  →  admin/admin123  →  obtienes token + rol ADMIN
2. POST /api/instructores  →  creas instructor
3. POST /api/auth/register  →  creas usuario ALUMNO
4. Login como alumno → obtienes token
5. POST /api/alumnos  →  creas alumno
6. POST /api/talleres  →  {"nombre":"Álgebra","cupo":30,"instructorId":1}
7. POST /api/talleres/1/inscribir/1  →  saga inscribe (o "Cupo lleno")
8. GET  /api/talleres/alumno/1  →  talleres del alumno
9. Detener ms-gestion-alumno → probar Circuit Breaker → fallback JSON
```
