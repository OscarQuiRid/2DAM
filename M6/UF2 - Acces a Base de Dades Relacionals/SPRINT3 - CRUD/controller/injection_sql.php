<?php
include("connection.php"); // Conexión a la base de datos

if (isset($_POST['injectionCode'])) {
    $injectionCode = trim($_POST['injectionCode']);

    // Bloqueo de caracteres peligrosos: solo se permiten letras, números, espacios, '(', ')', ';', y '\''.
    if (preg_match('/[^a-zA-Z0-9\s(),;.@=\'*]/', $injectionCode)) {
        echo json_encode(["error" => "Uso de caracteres no permitidos."]);
        exit;
    }

    // Permitir solo SELECT, INSERT y UPDATE en la tabla 'usuarios'
    if (!preg_match('/^(select|insert|update)\s+/i', $injectionCode)) {
        echo json_encode(["error" => "Operación no permitida."]);
        exit;
    }

    // Validación de uso exclusivo de la tabla 'usuarios'
    if (!preg_match('/\b(usuarios)\b/i', $injectionCode)) {
        echo json_encode(["error" => "Sentencia errónea."]);
        exit;
    }

    // Validación de columnas permitidas en la tabla 'usuarios' para INSERT y UPDATE
    if (preg_match('/\b(insert\s+into|update)\s+usuarios\s*\(([^)]+)\)/i', $injectionCode, $matches)) {
        $allowedColumns = ['nombre', 'apellido', 'edad', 'email'];
        $columns = array_map('trim', explode(',', $matches[2]));

        foreach ($columns as $column) {
            if (!in_array($column, $allowedColumns)) {
                echo json_encode(["error" => "Columnas no permitidas en la consulta."]);
                exit;
            }
        }
    }

    // Ejecución de la consulta si pasa todas las validaciones
    try {
        $stmt = $conexion->prepare($injectionCode);
        $stmt->execute();
        
        // Procesa SELECT para devolver resultados en JSON
        if (stripos($injectionCode, 'select') === 0) {
            $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
        } else {
            echo json_encode(["success" => "Operación realizada con éxito."]);
        }
    } catch (PDOException $e) {
        echo json_encode(["error" => "Error en la ejecución de la sentencia."]);
    }
}
?>
