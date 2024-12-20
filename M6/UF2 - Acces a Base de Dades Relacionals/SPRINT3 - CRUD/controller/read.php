<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include("connection.php");
include("functions.php");

$query = "";
$salida = array();
$query = "SELECT * FROM usuarios ";

if (isset($_POST["search"]["value"]) && $_POST["search"]["value"] != '') {
    $query .= 'WHERE nombre LIKE "%' . $_POST["search"]["value"] . '%" ';
    $query .= 'OR apellido LIKE "%' . $_POST["search"]["value"] . '%" ';
}

if (isset($_POST["order"])) {
    $query .= 'ORDER BY ' . $_POST['order']['0']['column'] .' '.$_POST["order"][0]['dir'] . ' ';        
}else{
    $query .= 'ORDER BY id DESC ';
}

if($_POST["length"] != -1){
    $query .= 'LIMIT ' . $_POST["start"] . ','. $_POST["length"];
}

$stmt = $conexion->prepare($query);
$stmt->execute();
$resultado = $stmt->fetchAll();
$datos = array();
$filtered_rows = $stmt->rowCount();
foreach($resultado as $fila){
    $sub_array = array();
    $sub_array[] = $fila["id"];
    $sub_array[] = $fila["nombre"];
    $sub_array[] = $fila["apellido"];
    $sub_array[] = $fila["edad"];
    $sub_array[] = $fila["email"];
    $sub_array[] = '<button type="button" name="editar" id="'.$fila["id"].'" class="btn btn-warning btn-xs editar">Editar</button>';
    $sub_array[] = '<button type="button" name="borrar" id="'.$fila["id"].'" class="btn btn-danger btn-xs borrar">Borrar</button>';
    $datos[] = $sub_array;
}

$salida = array(
    "draw"               => intval($_POST["draw"]),
    "recordsTotal"       => $filtered_rows,
    "recordsFiltered"    => obtener_todos_registros(),
    "data"               => $datos
);


header('Content-Type: application/json'); // Asegúrate de establecer el tipo de contenido
echo json_encode($salida);