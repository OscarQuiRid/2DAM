from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector

app = Flask(__name__)
CORS(app)

def get_db_connection():
    return mysql.connector.connect(
        host='localhost',
        user='root',
        password='',
        database='jmh'
    )

@app.route('/clientes', methods=['GET'])
def get_clientes():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT * FROM clientes')
    clientes = cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(clientes)

@app.route('/clientes', methods=['POST'])
def create_cliente():
    new_cliente = request.json
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        'INSERT INTO clientes (nombre, apellido, edad, email) VALUES (%s, %s, %s, %s)',
        (new_cliente['nombre'], new_cliente['apellido'], new_cliente['edad'], new_cliente['email'])
    )
    conn.commit()
    cursor.close()
    conn.close()
    return jsonify(new_cliente), 201

@app.route('/clientes/<int:cliente_id>', methods=['PUT'])
def update_cliente(cliente_id):
    updated_cliente = request.json
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        'UPDATE clientes SET nombre = %s, apellido = %s, edad = %s, email = %s WHERE id = %s',
        (updated_cliente['nombre'], updated_cliente['apellido'], updated_cliente['edad'], updated_cliente['email'], cliente_id)
    )
    conn.commit()
    cursor.close()
    conn.close()
    return jsonify(updated_cliente)

@app.route('/clientes/<int:cliente_id>', methods=['DELETE'])
def delete_cliente(cliente_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute('DELETE FROM clientes WHERE id = %s', (cliente_id,))
    conn.commit()
    cursor.close()
    conn.close()
    return '', 204

@app.route('/clientes/<int:cliente_id>', methods=['GET'])
def get_cliente(cliente_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT * FROM clientes WHERE id = %s', (cliente_id,))
    cliente = cursor.fetchone()
    cursor.close()
    conn.close()
    return jsonify(cliente)

if __name__ == '__main__':
    app.run(debug=True)