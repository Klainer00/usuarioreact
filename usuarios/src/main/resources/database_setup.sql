-- Crear base de datos
CREATE DATABASE IF NOT EXISTS usuarios_db;
USE usuarios_db;

-- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rut VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    region VARCHAR(100),
    comuna VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    rol_id BIGINT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Insertar roles por defecto
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('USUARIO', 'Usuario estándar'),
('VENDEDOR', 'Vendedor del sistema');

-- Insertar usuario admin por defecto (contraseña: admin123)
INSERT INTO usuarios (rut, nombre, apellido, email, password, estado, rol_id)
VALUES ('11.111.111-1', 'Admin', 'System', 'admin@example.com', '$2a$10$slYQmyNdGzin7olVN.4H2eI.guwq7N2w0Ww5CJCiL2Z9FkPqZsRxy', 'ACTIVO', 1);
