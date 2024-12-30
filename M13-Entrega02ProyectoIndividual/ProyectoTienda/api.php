<?php
// api.php
require_once 'conexion.php';

// Función para registrar un nuevo cliente (sólo correo y contraseña)
function registrarCliente($correo, $contraseña) {
    $conexion = conectar();
    $query = "INSERT INTO Clientes (CorreoElectronico, Contraseña) VALUES (?, ?)";
    $stmt = $conexion->prepare($query);
    $stmt->bind_param("ss", $correo, $contraseña);

    if ($stmt->execute()) {
        $respuesta = array("status" => "success", "message" => "Cliente registrado con éxito");
    } else {
        $respuesta = array("status" => "error", "message" => "Error al registrar el cliente");
    }

    $stmt->close();
    $conexion->close();
    return json_encode($respuesta);
}

// Función para actualizar los datos de un cliente
function actualizarCliente($clienteID, $nombre, $apellidos, $telefono, $direccion) {
    $conexion = conectar();
    $query = "UPDATE Clientes SET Nombre = ?, Apellidos = ?, Telefono = ?, Direccion = ? WHERE ClienteID = ?";
    $stmt = $conexion->prepare($query);
    $stmt->bind_param("ssssi", $nombre, $apellidos, $telefono, $direccion, $clienteID);

    if ($stmt->execute()) {
        $respuesta = array("status" => "success", "message" => "Cliente actualizado con éxito");
    } else {
        $respuesta = array("status" => "error", "message" => "Error al actualizar los datos del cliente");
    }

    $stmt->close();
    $conexion->close();
    return json_encode($respuesta);
}

// Función para obtener los datos de un cliente
function obtenerCliente($clienteID) {
    $conexion = conectar();
    $query = "SELECT * FROM Clientes WHERE ClienteID = ?";
    $stmt = $conexion->prepare($query);
    $stmt->bind_param("i", $clienteID);
    $stmt->execute();
    $resultado = $stmt->get_result();
    $cliente = $resultado->fetch_assoc();

    $stmt->close();
    $conexion->close();
    return json_encode($cliente);
}

// Función para obtener productos
function obtenerProductos() {
    $conexion = conectar();
    $query = "SELECT * FROM Productos";
    $resultado = $conexion->query($query);
    $productos = array();

    while ($row = $resultado->fetch_assoc()) {
        $productos[] = $row;
    }

    $conexion->close();
    return json_encode($productos);
}

// Función para obtener categorías
function obtenerCategorias() {
    $conexion = conectar();
    $query = "SELECT * FROM Categorias";
    $resultado = $conexion->query($query);
    $categorias = array();

    while ($row = $resultado->fetch_assoc()) {
        $categorias[] = $row;
    }

    $conexion->close();
    return json_encode($categorias);
}

// Función para obtener promociones activas
function obtenerPromociones() {
    $conexion = conectar();
    $query = "SELECT * FROM Promociones WHERE EstaActiva = 1";
    $resultado = $conexion->query($query);
    $promociones = array();

    while ($row = $resultado->fetch_assoc()) {
        // Asegurarnos de que la URL de la imagen esté correctamente formada
        if (isset($row['ImagenURL'])) {
            // Asegurarnos de que la URL esté bien formada (en caso de que haya algún problema en la base de datos)
            $row['ImagenURL'] = str_replace('\/', '/', $row['ImagenURL']);
        }
            // Convertir el valor booleano de 0 o 1 a true o false
    if (isset($row['EstaActiva'])) {
        $row['EstaActiva'] = $row['EstaActiva'] == 1 ? true : false;
    }
        $promociones[] = $row;
    }

    $conexion->close();

    // Usamos JSON_UNESCAPED_SLASHES para evitar que las barras se escapen
    return json_encode($promociones, JSON_UNESCAPED_SLASHES);
}

// Función para realizar un pedido
function realizarPedido($clienteID, $productos, $metodoPagoID) {
    $conexion = conectar();
    $conexion->begin_transaction(); // Inicio de la transacción

    try {
        // Insertar el pedido
        $queryPedido = "INSERT INTO Pedidos (ClienteID, Fecha, EstadoPedidoID) VALUES (?, CURRENT_DATE, 1)";
        $stmtPedido = $conexion->prepare($queryPedido);
        $stmtPedido->bind_param("i", $clienteID);
        $stmtPedido->execute();
        $pedidoID = $conexion->insert_id;

        // Insertar detalles de la factura
        $total = 0;
        foreach ($productos as $producto) {
            $subtotal = $producto['precio'] * $producto['cantidad'];
            $total += $subtotal;

            $queryDetalle = "INSERT INTO DetalleFactura (FacturaID, ProductoID, Cantidad, PrecioUnitario, Subtotal, Total) 
                             VALUES (?, ?, ?, ?, ?, ?)";
            $stmtDetalle = $conexion->prepare($queryDetalle);
            $stmtDetalle->bind_param("iiiddd", $pedidoID, $producto['id'], $producto['cantidad'], $producto['precio'], $subtotal, $subtotal);
            $stmtDetalle->execute();
        }

        // Insertar la factura
        $queryFactura = "INSERT INTO Facturas (ClienteID, MetodoPagoID, Total) VALUES (?, ?, ?)";
        $stmtFactura = $conexion->prepare($queryFactura);
        $stmtFactura->bind_param("iid", $clienteID, $metodoPagoID, $total);
        $stmtFactura->execute();

        $conexion->commit(); // Confirmar transacción
        $respuesta = array("status" => "success", "message" => "Pedido realizado con éxito");
    } catch (Exception $e) {
        $conexion->rollback(); // Revertir transacción
        $respuesta = array("status" => "error", "message" => "Error al realizar el pedido: " . $e->getMessage());
    }

    $conexion->close();
    return json_encode($respuesta);
}

// Función para actualizar el estado del pedido
function actualizarEstadoPedido($pedidoID, $estadoPedidoID) {
    $conexion = conectar();
    $query = "UPDATE Pedidos SET EstadoPedidoID = ? WHERE PedidoID = ?";
    $stmt = $conexion->prepare($query);
    $stmt->bind_param("ii", $estadoPedidoID, $pedidoID);

    if ($stmt->execute()) {
        $respuesta = array("status" => "success", "message" => "Estado del pedido actualizado con éxito");
    } else {
        $respuesta = array("status" => "error", "message" => "Error al actualizar el estado del pedido");
    }

    $stmt->close();
    $conexion->close();
    return json_encode($respuesta);
}

// Llamar la función dependiendo de la acción solicitada
$accion = $_GET['accion'] ?? '';

switch ($accion) {
    case 'registrarCliente':
        echo registrarCliente($_POST['correo'], $_POST['contraseña']);
        break;
    case 'actualizarCliente':
        echo actualizarCliente($_POST['clienteID'], $_POST['nombre'], $_POST['apellidos'], $_POST['telefono'], $_POST['direccion']);
        break;
    case 'obtenerCliente':
        echo obtenerCliente($_GET['clienteID']);
        break;
    case 'obtenerProductos':
        echo obtenerProductos();
        break;
    case 'obtenerCategorias':
        echo obtenerCategorias();
        break;
    case 'obtenerPromociones':
        echo obtenerPromociones();
        break;
    case 'realizarPedido':
        echo realizarPedido($_POST['clienteID'], json_decode($_POST['productos'], true), $_POST['metodoPagoID']);
        break;
    case 'actualizarEstadoPedido':
        echo actualizarEstadoPedido($_POST['pedidoID'], $_POST['estadoPedidoID']);
        break;
    default:
        echo json_encode(array("status" => "error", "message" => "Acción no encontrada"));
        break;
}
?>