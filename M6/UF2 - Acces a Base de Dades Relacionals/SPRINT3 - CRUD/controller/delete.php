<?php
include("connection.php");

if(isset($_POST["id_usuario"]))
{
	$stmt = $conexion->prepare(
		"DELETE FROM usuarios WHERE id = :id"
	);
	$resultado = $stmt->execute(
		array(
			':id'	=>	$_POST["id_usuario"]
		)
	);
	
	if(!empty($resultado))
	{
		echo 'Registro borrado';
	}
}
?>