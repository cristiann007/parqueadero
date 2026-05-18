-- ======================================================
-- SCRIPT COMPLETO: ESTRUCTURA Y DATOS DE PRUEBA
-- BASE DE DATOS: parqueadero_db
-- ======================================================

-- 1. Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS parqueadero_db;
USE parqueadero_db;

-- 2. Crear tabla 'parqueaderos'
CREATE TABLE IF NOT EXISTS parqueaderos (
    id_parqueadero INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    capacidad INT NOT NULL,
    tarifa_por_hora DECIMAL(10, 2) NOT NULL
);

-- 3. Crear tabla 'espacios'
CREATE TABLE IF NOT EXISTS espacios (
    id_espacio INT AUTO_INCREMENT PRIMARY KEY,
    id_parqueadero INT NOT NULL,
    numero_espacio VARCHAR(10) NOT NULL,
    estado BOOLEAN DEFAULT TRUE, -- TRUE = Libre, FALSE = Ocupado
    FOREIGN KEY (id_parqueadero) REFERENCES parqueaderos(id_parqueadero) ON DELETE CASCADE
);

-- 4. Crear tabla 'carros'
CREATE TABLE IF NOT EXISTS carros (
    id_carro INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    color VARCHAR(30),
    id_espacio INT,
    fecha_ingreso DATETIME,
    FOREIGN KEY (id_espacio) REFERENCES espacios(id_espacio) ON DELETE SET NULL
);

-- 5. Crear tabla 'historial_salidas'
CREATE TABLE IF NOT EXISTS historial_salidas (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    color VARCHAR(30),
    espacio_usado INT,
    fecha_ingreso DATETIME,
    fecha_salida DATETIME,
    horas_estadia INT,
    total_pagado DECIMAL(10, 2),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================================================
-- DATOS DE PRUEBA
-- ======================================================

-- Insertar un Parqueadero de Prueba
INSERT INTO parqueaderos (nombre, direccion, capacidad, tarifa_por_hora) 
VALUES ('Parqueadero Central Demo', 'Av. Principal #123, Centro', 50, 2500.00);

-- Insertar Espacios de Prueba (Asociados al ID 1 del parqueadero anterior)
INSERT INTO espacios (id_parqueadero, numero_espacio, estado) VALUES 
(1, 'A-01', TRUE),
(1, 'A-02', TRUE),
(1, 'A-03', TRUE),
(1, 'A-04', TRUE),
(1, 'A-05', TRUE),
(1, 'A-06', TRUE),
(1, 'A-07', TRUE),
(1, 'A-08', TRUE),
(1, 'A-09', TRUE),
(1, 'A-10', TRUE);

-- Insertar Carros de Prueba Estacionados
-- Carro 1: Toyota Corolla en espacio A-01 (ID 1)
INSERT INTO carros (placa, marca, modelo, color, id_espacio, fecha_ingreso) 
VALUES ('ABC-123', 'Toyota', 'Corolla', 'Rojo', 1, NOW());

-- Actualizar espacio A-01 a OCUPADO
UPDATE espacios SET estado = FALSE WHERE id_espacio = 1;

-- Carro 2: Chevrolet Spark en espacio A-02 (ID 2)
INSERT INTO carros (placa, marca, modelo, color, id_espacio, fecha_ingreso) 
VALUES ('XYZ-789', 'Chevrolet', 'Spark', 'Azul', 2, NOW());

-- Actualizar espacio A-02 a OCUPADO
UPDATE espacios SET estado = FALSE WHERE id_espacio = 2;

-- Carro 3: Ford Fiesta en espacio A-03 (ID 3) - Opcional
INSERT INTO carros (placa, marca, modelo, color, id_espacio, fecha_ingreso) 
VALUES ('DEF-456', 'Ford', 'Fiesta', 'Blanco', 3, NOW());

-- Actualizar espacio A-03 a OCUPADO
UPDATE espacios SET estado = FALSE WHERE id_espacio = 3;

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================
