<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include("connection.php");


if (isset($_POST["id_usuario"])) {
    $salida = array();
    $stmt = $conexion->prepare("SELECT * FROM usuarios WHERE id = '".$_POST["id_usuario"]."' LIMIT 1");
    $stmt->execute();
    $resultado = $stmt->fetchAll();
    foreach($resultado as $fila){
        $salida["nombre"] = $fila["nombre"];
        $salida["apellido"] = $fila["apellido"];
        $salida["edad"] = $fila["edad"];
        $salida["email"] = $fila["email"];
    }

    echo json_encode($salida);
}