@echo off
echo ========================================
echo Iniciando Sistema de Gestion Academica
echo ========================================

echo.
echo [1/6] Levantando PostgreSQL...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ERROR: Docker no esta corriendo. Inicia Docker Desktop e intentalo de nuevo.
    pause
    exit /b 1
)
echo PostgreSQL listo.
timeout /t 5 /nobreak >nul

echo.
echo [2/6] Iniciando Config Server (puerto 8888)...
start "Config Server" cmd /c "cd /d %~dp0ms-admin-config-server && .\mvnw.cmd spring-boot:run"
timeout /t 20 /nobreak >nul

echo.
echo [3/6] Iniciando Registry Server (puerto 8761)...
start "Registry Server" cmd /c "cd /d %~dp0ms-admin-registry-server && .\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo.
echo [4/6] Iniciando API Gateway (puerto 8080)...
start "API Gateway" cmd /c "cd /d %~dp0ms-admin-api-gateway && .\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo.
echo [5/6] Iniciando Instructor (puerto 8081)...
start "Instructor" cmd /c "cd /d %~dp0ms-gestion-instructor && .\mvnw.cmd spring-boot:run"

echo.
echo [6/6] Iniciando Alumno y Taller...
start "Alumno" cmd /c "cd /d %~dp0ms-gestion-alumno && .\mvnw.cmd spring-boot:run"
start "Taller" cmd /c "cd /d %~dp0ms-gestion-taller && .\mvnw.cmd spring-boot:run"

echo.
echo ========================================
echo Todos los servicios iniciandose.
echo Abre http://localhost:8761 para verificar.
echo ========================================
pause
