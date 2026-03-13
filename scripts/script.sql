-- =========================
-- TABLA CLIENTE
-- =========================
CREATE TABLE cliente (
                         id INTEGER PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellidos VARCHAR(100) NOT NULL,
                         ciudad VARCHAR(100) NOT NULL
);

-- =========================
-- TABLA PRODUCTO
-- =========================
CREATE TABLE producto (
                          id INTEGER PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          tipoProducto VARCHAR(100) NOT NULL
);

-- =========================
-- TABLA SUCURSAL
-- =========================
CREATE TABLE sucursal (
                          id INTEGER PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          ciudad VARCHAR(100) NOT NULL
);

-- =========================
-- TABLA INSCRIPCION
-- RELACION CLIENTE - PRODUCTO
-- =========================
CREATE TABLE inscripcion (
                             idProducto INTEGER NOT NULL,
                             idCliente INTEGER NOT NULL,

                             PRIMARY KEY (idProducto, idCliente),

                             FOREIGN KEY (idProducto)
                                 REFERENCES producto(id),

                             FOREIGN KEY (idCliente)
                                 REFERENCES cliente(id)
);

-- =========================
-- TABLA DISPONIBILIDAD
-- RELACION SUCURSAL - PRODUCTO
-- =========================
CREATE TABLE disponibilidad (
                                idSucursal INTEGER NOT NULL,
                                idProducto INTEGER NOT NULL,

                                PRIMARY KEY (idSucursal, idProducto),

                                FOREIGN KEY (idSucursal)
                                    REFERENCES sucursal(id),

                                FOREIGN KEY (idProducto)
                                    REFERENCES producto(id)
);

-- =========================
-- TABLA VISITAN
-- RELACION CLIENTE - SUCURSAL
-- =========================
CREATE TABLE visitan (
                         idSucursal INTEGER NOT NULL,
                         idCliente INTEGER NOT NULL,
                         fechaVisita DATE NOT NULL,

                         PRIMARY KEY (idSucursal, idCliente),

                         FOREIGN KEY (idSucursal)
                             REFERENCES sucursal(id),

                         FOREIGN KEY (idCliente)
                             REFERENCES cliente(id)
);

-- 1. TABLA PRODUCTO (Los 5 fondos requeridos)
INSERT INTO producto (id, nombre, tipoProducto) VALUES
(1, 'FPV_BTG_PACTUAL_RECAUDADORA', 'FPV'),
(2, 'FPV_BTG_PACTUAL_ECOPETROL', 'FPV'),
(3, 'DEUDAPRIVADA', 'FIC'),
(4, 'FDO-ACCIONES', 'FIC'),
(5, 'FPV_BTG_PACTUAL_DINAMICA', 'FPV');

-- 2. TABLA SUCURSAL (10 sedes para cubrir cobertura)
INSERT INTO sucursal (id, nombre, ciudad) VALUES
(1, 'Sede Principal Bogota', 'Bogotá'),
(2, 'Sede Poblado Medellin', 'Medellín'),
(3, 'Centro Financiero Cali', 'Cali'),
(4, 'Banca Privada Barranquilla', 'Barranquilla'),
(5, 'Oficina Bucaramanga', 'Bucaramanga'),
(6, 'Sucursal Pereira', 'Pereira'),
(7, 'Centro Inversion Cartagena', 'Cartagena'),
(8, 'Oficina Manizales', 'Manizales'),
(9, 'Sucursal Cucuta', 'Cúcuta'),
(10, 'Canal Digital BTG', 'Virtual');

-- 3. TABLA CLIENTE (Necesaria para integridad referencial)
INSERT INTO cliente (id, nombre, apellidos) VALUES
(101, 'Carlos', 'Restrepo', 'Bogotá'), (102, 'Ana Maria', 'Silva', 'Medellín'), (103, 'Jorge', 'Tadeo', 'Cali'),
(104, 'Lucia', 'Fernandez', 'Bogotá'), (105, 'Andres', 'Cepeda', 'Medellín'), (106, 'Marta', 'Lucia', 'Cali'),
(107, 'Roberto', 'Gomez', 'Bogotá'), (108, 'Elena', 'Poniatowska', 'Medellín'), (109, 'Gabriel', 'Garcia', 'Cali'), (110, 'Sofia', 'Vergara', 'Bogotá');

-- 4. TABLA INSCRIPCION (10 inversiones - Los clientes eligen entre los 5 fondos)
INSERT INTO inscripcion (idProducto, idCliente) VALUES
(1, 101), (1, 102), -- Dos clientes en Recaudadora
(2, 103), (2, 104), -- Dos en Ecopetrol
(3, 105), (3, 106), -- Dos en Deuda Privada
(4, 107), (4, 108), -- Dos en Fdo-Acciones
(5, 109), (5, 110); -- Dos en Dinámica

-- 5. TABLA DISPONIBILIDAD (10 registros - Distribución de fondos por sucursal)
INSERT INTO disponibilidad (idSucursal, idProducto) VALUES
(10, 1), (10, 2), (10, 3), (10, 4), (10, 5), -- El canal digital ofrece los 5
(1, 1), (1, 4), -- Bogotá ofrece Recaudadora y Acciones
(2, 2), (2, 3), -- Medellín ofrece Ecopetrol y Deuda Privada
(3, 5);         -- Cali ofrece Dinámica

-- 6. TABLA VISITAN (10 registros de visitas de clientes)
INSERT INTO visitan (idSucursal, idCliente, fechaVisita) VALUES
(1, 101, '2024-03-01'), (2, 102, '2024-03-02'),
(3, 103, '2024-03-03'), (4, 104, '2024-03-04'),
(10, 105, '2024-03-05'), (10, 106, '2024-03-06'),
(7, 107, '2024-03-07'), (8, 108, '2024-03-08'),
(9, 109, '2024-03-09'), (10, 110, '2024-03-10');

SELECT DISTINCT
    c.nombre,
    c.apellidos -- Corregido a plural según tu diagrama
FROM cliente AS c
         INNER JOIN inscripcion AS i ON c.id = i.idCliente
         INNER JOIN producto AS p ON i.idProducto = p.id
         INNER JOIN disponibilidad AS d ON p.id = d.idProducto
         INNER JOIN sucursal AS s ON d.idSucursal = s.id
         INNER JOIN visitan AS v ON s.id = v.idSucursal AND c.id = v.idCliente;