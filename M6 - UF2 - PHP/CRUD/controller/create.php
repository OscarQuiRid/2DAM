<?php

include("connection.php");

if ($_POST["operacion"] == "Crear") {
    $stmt = $conexion->prepare("INSERT INTO usuarios (nombre, apellido, edad, email) VALUES(:nombre, :apellido, :edad, :email)");

    $resultado = $stmt->execute(
        array(
            ':nombre'    => $_POST["nombre"],
            ':apellido'    => $_POST["apellido"],
            ':edad'    => $_POST["edad"],
            ':email'    => $_POST["email"],
        )
    );

    if (!empty($resultado)) {
        echo 'Registro creado';
    }
}


if ($_POST["operacion"] == "Editar") {

    $stmt = $conexion->prepare("UPDATE usuarios SET nombre=:nombre, apellido=:apellido, edad=:edad, email=:email WHERE id = :id");

    $resultado = $stmt->execute(
        array(
            ':nombre'    => $_POST["nombre"],
            ':apellido'    => $_POST["apellido"],
            ':edad'    => $_POST["edad"],
            ':email'    => $_POST["email"],
            ':id'         => $_POST["id_usuario"],
        )
    );

    if (!empty($resultado)) {
        echo 'Registro creado';
    }
}