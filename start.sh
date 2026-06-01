#!/bin/bash
echo "========================================"
echo "Iniciando Sistema de Gestion Academica"
echo "========================================"

echo ""
echo "[1/6] Levantando PostgreSQL..."
docker-compose up -d
if [ $? -ne 0 ]; then
    echo "ERROR: Docker no esta corriendo."
    exit 1
fi
echo "PostgreSQL listo."
sleep 5

echo ""
echo "[2/6] Iniciando Config Server (puerto 8888)..."
cd ms-admin-config-server && ./mvnw spring-boot:run &
sleep 20
cd ..

echo ""
echo "[3/6] Iniciando Registry Server (puerto 8761)..."
cd ms-admin-registry-server && ./mvnw spring-boot:run &
sleep 15
cd ..

echo ""
echo "[4/6] Iniciando API Gateway (puerto 8080)..."
cd ms-admin-api-gateway && ./mvnw spring-boot:run &
sleep 15
cd ..

echo ""
echo "[5/6] Iniciando Instructor (puerto 8081)..."
cd ms-gestion-instructor && ./mvnw spring-boot:run &
cd ..

echo ""
echo "[6/6] Iniciando Alumno y Taller..."
cd ms-gestion-alumno && ./mvnw spring-boot:run &
cd ..
cd ms-gestion-taller && ./mvnw spring-boot:run &
cd ..

echo ""
echo "========================================"
echo "Todos los servicios iniciandose."
echo "Abre http://localhost:8761 para verificar."
echo "========================================"
