document.addEventListener('DOMContentLoaded', function() {
    const apiUrl = 'http://localhost:5000/clientes'; // URL de tu API

    const clientesTableBody = document.querySelector('#clientesTable tbody');
    const addClienteForm = document.getElementById('addClienteForm');
    const nombreInput = document.getElementById('nombre');
    const apellidoInput = document.getElementById('apellido');
    const edadInput = document.getElementById('edad');
    const emailInput = document.getElementById('email');

    const editModal = document.getElementById('editModal');
    const editClienteForm = document.getElementById('editClienteForm');
    const editNombreInput = document.getElementById('editNombre');
    const editApellidoInput = document.getElementById('editApellido');
    const editEdadInput = document.getElementById('editEdad');
    const editEmailInput = document.getElementById('editEmail');
    const closeModal = document.getElementsByClassName('close')[0];

    let editingClienteId = null;

    // Función para obtener y mostrar los clientes
    function fetchClientes() {
        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                clientesTableBody.innerHTML = '';
                data.forEach(cliente => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${cliente.id}</td>
                        <td>${cliente.nombre}</td>
                        <td>${cliente.apellido}</td>
                        <td>${cliente.edad}</td>
                        <td>${cliente.email}</td>
                        <td>
                            <button onclick="editCliente(${cliente.id})">Editar</button>
                            <button onclick="deleteCliente(${cliente.id})">Eliminar</button>
                        </td>
                    `;
                    clientesTableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error al obtener los clientes:', error));
    }

    // Función para agregar un nuevo cliente
    addClienteForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const nombre = nombreInput.value;
        const apellido = apellidoInput.value;
        const edad = edadInput.value;
        const email = emailInput.value;

        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nombre: nombre, apellido: apellido, edad: edad, email: email })
        })
        .then(response => response.json())
        .then(data => {
            alert('Cliente añadido con éxito');
            fetchClientes();
            addClienteForm.reset();
        })
        .catch(error => console.error('Error al agregar el cliente:', error));
    });

    // Función para eliminar un cliente
    window.deleteCliente = function(id) {
        fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert('Cliente eliminado con éxito');
            fetchClientes();
        })
        .catch(error => console.error('Error al eliminar el cliente:', error));
    };

    // Función para abrir el modal de edición
    window.editCliente = function(id) {
        fetch(`${apiUrl}/${id}`)
            .then(response => response.json())
            .then(cliente => {
                editNombreInput.value = cliente.nombre;
                editApellidoInput.value = cliente.apellido;
                editEdadInput.value = cliente.edad;
                editEmailInput.value = cliente.email;
                editingClienteId = id;
                editModal.style.display = 'block';
            })
            .catch(error => console.error('Error al obtener el cliente:', error));
    };

    // Función para cerrar el modal
    closeModal.onclick = function() {
        editModal.style.display = 'none';
    };

    // Cerrar el modal si se hace clic fuera de él
    window.onclick = function(event) {
        if (event.target == editModal) {
            editModal.style.display = 'none';
        }
    };

    // Función para actualizar un cliente desde el modal
    editClienteForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const nombre = editNombreInput.value;
        const apellido = editApellidoInput.value;
        const edad = editEdadInput.value;
        const email = editEmailInput.value;

        fetch(`${apiUrl}/${editingClienteId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nombre: nombre, apellido: apellido, edad: edad, email: email })
        })
        .then(response => response.json())
        .then(data => {
            alert('Cliente actualizado con éxito');
            fetchClientes();
            editModal.style.display = 'none';
        })
        .catch(error => console.error('Error al actualizar el cliente:', error));
    });

    // Obtener y mostrar los clientes al cargar la página
    fetchClientes();
});