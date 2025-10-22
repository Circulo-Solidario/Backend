INSERT IGNORE INTO rol (id, nombre, descripcion, activo) VALUES (1, 'INVITADO', 'Usuario regular', true);
INSERT IGNORE INTO rol (id, nombre, descripcion, activo) VALUES (2, 'DONANTE', 'Usuario donador', true);
INSERT IGNORE INTO rol (id, nombre, descripcion, activo) VALUES (3, 'DONATARIO', 'Usuario donatario', true);
INSERT IGNORE INTO rol (id, nombre, descripcion, activo) VALUES (4, 'VOLUNTARIO OBSERVADOR', 'Usuario voluntario', true);
INSERT IGNORE INTO rol (id, nombre, descripcion, activo) VALUES (5, 'ADMINISTRADOR', 'Administrador del sistema', true);

INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (1, 'Ropa', 'Vestimenta y accesorios de todo tipo');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (2, 'Calzado', 'Zapatos, zapatillas y todo tipo de calzado');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (3, 'Alimentos no perecederos', 'Alimentos envasados y de larga duración');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (4, 'Artículos de higiene', 'Productos de aseo personal y limpieza');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (5, 'Abrigos', 'Frazadas, mantas y ropa de abrigo');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (6, 'Medicamentos', 'Medicamentos y artículos de primeros auxilios');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (7, 'Artículos para bebés', 'Pañales, ropa y artículos para bebés');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (8, 'Artículos escolares', 'Útiles escolares y material educativo');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (9, 'Mobiliario', 'Muebles y artículos para el hogar');
INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (10, 'Otros', 'Otros artículos no categorizados');
