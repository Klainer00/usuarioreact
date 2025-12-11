@echo off
rem Abre nuevas ventanas y arranca cada servicio con mvn spring-boot:run
setlocal enabledelayedexpansion

start "auth-service" cmd /k "cd auth-service && mvn -DskipTests spring-boot:run"
start "productos-service" cmd /k "cd productos-service && mvn -DskipTests spring-boot:run"
start "pedidos-service" cmd /k "cd pedidos-service && mvn -DskipTests spring-boot:run"
start "contacto-service" cmd /k "cd contacto-service && mvn -DskipTests spring-boot:run"
start "ecommerce-gateway" cmd /k "cd ecommerce-gateway && mvn -DskipTests spring-boot:run"

echo Servicios arrancando en ventanas separadas...
pause
