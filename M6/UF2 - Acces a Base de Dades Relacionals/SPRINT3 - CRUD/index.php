<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="styles/styles.css">
    <title>CRUD Oscar</title>
</head>

<body>

    <div class="container fondo">

        <h1 class="text-center">CRUD OSCAR QUINTANILLA</h1>
        <div class="row">
            <div class="col-2 offset-10">
                <div class="text-center">
                    <!-- Button trigger modal -->
                    <button type="button" class="btn btn-primary w-100" data-bs-toggle="modal"
                        data-bs-target="#modalUsuario" id="botonCrear">
                        <i class="bi bi-plus-circle-fill"></i> Crear
                    </button>
                </div>
            </div>
        </div>
        <br />
        <br />

        <div class="table-responsive">
            <table id="datos_usuario" class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>nombre</th>
                        <th>apellido</th>
                        <th>edad</th>
                        <th>Email</th>
                        <th>Editar</th>
                        <th>Borrar</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="mt-4">
        <h4>Inyección de Código</h4>
        <textarea id="injectionCode" class="form-control" rows="10" placeholder="Ingrese su código aquí"
            style="resize: none;"></textarea>
        <button id="testInjection" class="btn btn-danger mt-2">Probar Inyección</button>
        <div id="injectionResult" class="mt-2"></div>
        <div class="mt-2">
            <h5>Resultado de la Inyección</h5>
            <textarea id="injectionResultMessage" class="form-control" rows="10"
                placeholder="El resultado se mostrará aquí..." readonly></textarea>
        </div>
    </div>


    <!-- formulario -->
    <div class="modal fade" id="modalUsuario" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Crear Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form method="POST" id="formulario" enctype="multipart/form-data">
                    <div class="modal-content">
                        <div class="modal-body">
                            <label for="nombre">Ingrese el nombre</label>
                            <input type="text" name="nombre" id="nombre" class="form-control">
                            <br />

                            <label for="apellido">Ingrese los apellido</label>
                            <input type="text" name="apellido" id="apellido" class="form-control">
                            <br />

                            <label for="edad">Ingrese el teléfono</label>
                            <input type="text" name="edad" id="edad" class="form-control">
                            <br />

                            <label for="email">Ingrese el email</label>
                            <input type="email" name="email" id="email" class="form-control">
                            <br />

                        </div>

                        <div class="modal-footer">
                            <input type="hidden" name="id_usuario" id="id_usuario">
                            <input type="hidden" name="operacion" id="operacion">
                            <input type="submit" name="action" id="action" class="btn btn-success" value="Crear">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>


    <script src="https://code.jquery.com/jquery-3.6.0.min.js"
        integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script src="//cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#botonCrear").click(function () {
                $("#formulario")[0].reset();
                $(".modal-title").text("Crear Usuario");
                $("#action").val("Crear");
                $("#operacion").val("Crear");
            });

            var dataTable = $('#datos_usuario').DataTable({
                "processing": true,
                "serverSide": true,
                "order": [],
                "ajax": {
                    url: "controller/read.php",
                    type: "POST"
                },
                "columnsDefs": [
                    {
                        "targets": [0, 3, 4],
                        "orderable": false,
                    },
                ],
                "language": {
                    "decimal": "",
                    "emptyTable": "No hay registros",
                    "info": "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
                    "infoEmpty": "Mostrando 0 to 0 of 0 Entradas",
                    "infoFiltered": "(Filtrado de _MAX_ total entradas)",
                    "infoPostFix": "",
                    "thousands": ",",
                    "lengthMenu": "Mostrar _MENU_ Entradas",
                    "loadingRecords": "Cargando...",
                    "processing": "Procesando...",
                    "search": "Buscar:",
                    "zeroRecords": "Sin resultados encontrados",
                    "paginate": {
                        "first": "Primero",
                        "last": "Ultimo",
                        "next": "Siguiente",
                        "previous": "Anterior"
                    }
                }
            });

            //Aquí código inserción
            $(document).on('submit', '#formulario', function (event) {
                event.preventDefault();
                var nombres = $('#nombre').val();
                var apellido = $('#apellido').val();
                var edad = $('#edad').val();
                var email = $('#email').val();
                if (nombres != '' && apellido != '' && email != '') {
                    $.ajax({
                        url: "controller/create.php",
                        method: 'POST',
                        data: new FormData(this),
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            alert(data);
                            $('#formulario')[0].reset();
                            $('#modalUsuario').modal('hide');
                            dataTable.ajax.reload();
                        }
                    });
                }
                else {
                    alert("Algunos campos son obligatorios");
                }
            });

            //Funcionalida de editar
            $(document).on('click', '.editar', function () {
                var id_usuario = $(this).attr("id");
                $.ajax({
                    url: "controller/update.php",
                    method: "POST",
                    data: { id_usuario: id_usuario },
                    dataType: "json",
                    success: function (data) {
                        //console.log(data);				
                        $('#modalUsuario').modal('show');
                        $('#nombre').val(data.nombre);
                        $('#apellido').val(data.apellido);
                        $('#edad').val(data.edad);
                        $('#email').val(data.email);
                        $('.modal-title').text("Editar Usuario");
                        $('#id_usuario').val(id_usuario);
                        $('#action').val("Editar");
                        $('#operacion').val("Editar");
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(textStatus, errorThrown);
                    }
                })
            });

            //Funcionalida de borrar
            $(document).on('click', '.borrar', function () {
                var id_usuario = $(this).attr("id");
                if (confirm("Esta seguro de borrar este registro:" + id_usuario)) {
                    $.ajax({
                        url: "controller/delete.php",
                        method: "POST",
                        data: { id_usuario: id_usuario },
                        success: function (data) {
                            alert(data);
                            dataTable.ajax.reload();
                        }
                    });
                }
                else {
                    return false;
                }
            });
            
            $(document).ready(function () {
                // Evento del botón para probar inyección
                $('#testInjection').click(function () {
                    const injectionCode = $('#injectionCode').val();

                    // Envío seguro de código para probar inyección
                    $.ajax({
                        url: 'controller/injection_sql.php',
                        type: 'POST',
                        data: { injectionCode: injectionCode },
                        dataType: 'json',
                        success: function (response) {
                            // Mostrar resultados en el área de texto
                            if (response.error) {
                                $('#injectionResultMessage').val("Error: Consulta no permitida.");
                            } else if (response.success) {
                                $('#injectionResultMessage').val(response.success);
                            } else {
                                $('#injectionResultMessage').val(JSON.stringify(response, null, 2));
                            }
                        },
                        error: function (xhr, status, error) {
                            $('#injectionResultMessage').val("Error de conexión.");
                        }
                    });
                });
            });
        });         
    </script>

</body>

</html>