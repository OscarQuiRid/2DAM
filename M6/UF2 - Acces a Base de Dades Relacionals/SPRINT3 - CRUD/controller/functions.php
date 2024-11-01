<?php
function obtener_todos_registros(){
    include('connection.php');
    $stmt = $conexion->prepare("SELECT * FROM usuarios");
    $stmt->execute();
    $resultado = $stmt->fetchAll(); 
    return $stmt->rowCount();       
}