1.- Crear una nueva base de datos.

    ddbb nombre = newcrud.

2.- crear tabla e insertar datos BORRA LA TABLA USUARIOS SI LA TIENES

DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    edad INT,
    email VARCHAR(100)
); 

INSERT INTO usuarios (nombre, apellido, edad, email) VALUES
('Juan', 'Pérez', 30, 'juan.perez@example.com'),
('María', 'García', 25, 'maria.garcia@example.com'),
('Carlos', 'López', 28, 'carlos.lopez@example.com'),
('Ana', 'Martínez', 22, 'ana.martinez@example.com'),
('Luis', 'Hernández', 35, 'luis.hernandez@example.com');


3.- insercion de datos por formulario.

Miguel
Sánchez
28
miguel.sanchez@example.com

Camila
Mendoza
31
camila.mendoza@example.com

Alberto
López
37
alberto.lopez@example.com

Natalia
Cruz
24
natalia.cruz@example.com

Gonzalo
Hernández
45
gonzalo.hernandez@example.com

Teresa
Salazar
34
teresa.salazar@example.com

Fernando
Ramírez
33
fernando.ramirez@example.com

Isabel
Soto
30
isabel.soto@example.com

Nicolás
Ponce
29
nicolas.ponce@example.com

Valentina
Ríos
36
valentina.rios@example.com

4.- test insercion de datos CORRECTOS por sql.(filtra una gran cantidad de datos par no poder manipular tablas ni actuar sobre las tablas.)
    4.1.- insert

        INSERT INTO usuarios (nombre, apellido, edad, email) VALUES ('John', 'Doe', 25, 'johndoe@example.com');
        INSERT INTO usuarios (nombre, apellido, edad, email) VALUES ('Alice', 'Smith', 18, 'alice.smith@example.com');
        INSERT INTO usuarios (nombre, apellido, edad, email) VALUES ('Christopher', 'Johnson', 30, 'chris.johnson@example.com');
        INSERT INTO usuarios (nombre, apellido, edad, email) VALUES ('A', 'B', 100, 'user100@example.com');
        INSERT INTO usuarios (nombre, apellido, edad, email) VALUES ('Tom', '', 22, 'tom@example.com');


    4.2.- updates

        UPDATE usuarios SET nombre = 'Jonathan' WHERE id = 1;
        UPDATE usuarios SET edad = 35 WHERE id = 2;
        UPDATE usuarios SET email = 'newemail@example.com' WHERE id = 3;
        UPDATE usuarios SET nombre = 'Michael', apellido = 'Brown' WHERE id = 4;
        UPDATE usuarios SET apellido = '' WHERE id = 5;


    4.3.- updates

        SELECT * FROM usuarios;
        SELECT * FROM usuarios WHERE edad = 25;
        SELECT * FROM usuarios WHERE email = 'johndoe@example.com';
        SELECT * FROM usuarios WHERE edad > 30;
        SELECT nombre, apellido FROM usuarios;



