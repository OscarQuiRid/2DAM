<?php
$host = "localhost";
$usuario = "root";
$clave = "";
$baseDeDatos = "tienda";

function conectar() {
    global $host, $usuario, $clave, $baseDeDatos;
    $conexion = new mysqli($host, $usuario, $clave, $baseDeDatos);

    if ($conexion->connect_error) {
        die("Error de conexión: " . $conexion->connect_error);
    }
    return $conexion;
}
?>