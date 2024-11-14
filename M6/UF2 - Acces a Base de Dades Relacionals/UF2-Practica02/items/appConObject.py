from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/jmh'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

class Cliente(db.Model):
    __tablename__ = 'clientes'
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nombre = db.Column(db.String(100), nullable=False)
    apellido = db.Column(db.String(100), nullable=False)
    edad = db.Column(db.Integer, nullable=False)
    email = db.Column(db.String(100), nullable=False)

@app.route('/clientes', methods=['GET'])
def get_clientes():
    clientes = Cliente.query.all()
    clientes_list = [{'id': cliente.id, 'nombre': cliente.nombre, 'apellido': cliente.apellido, 'edad': cliente.edad, 'email': cliente.email} for cliente in clientes]
    return jsonify(clientes_list)

@app.route('/clientes', methods=['POST'])
def create_cliente():
    data = request.json
    new_cliente = Cliente(
        nombre=data['nombre'],
        apellido=data['apellido'],
        edad=data['edad'],
        email=data['email']
    )
    db.session.add(new_cliente)
    db.session.commit()
    return jsonify({'id': new_cliente.id, 'nombre': new_cliente.nombre, 'apellido': new_cliente.apellido, 'edad': new_cliente.edad, 'email': new_cliente.email}), 201

@app.route('/clientes/<int:cliente_id>', methods=['PUT'])
def update_cliente(cliente_id):
    data = request.json
    cliente = Cliente.query.get_or_404(cliente_id)
    cliente.nombre = data['nombre']
    cliente.apellido = data['apellido']
    cliente.edad = data['edad']
    cliente.email = data['email']
    db.session.commit()
    return jsonify({'id': cliente.id, 'nombre': cliente.nombre, 'apellido': cliente.apellido, 'edad': cliente.edad, 'email': cliente.email})

@app.route('/clientes/<int:cliente_id>', methods=['DELETE'])
def delete_cliente(cliente_id):
    cliente = Cliente.query.get_or_404(cliente_id)
    db.session.delete(cliente)
    db.session.commit()
    return '', 204

@app.route('/clientes/<int:cliente_id>', methods=['GET'])
def get_cliente(cliente_id):
    cliente = Cliente.query.get_or_404(cliente_id)
    return jsonify({'id': cliente.id, 'nombre': cliente.nombre, 'apellido': cliente.apellido, 'edad': cliente.edad, 'email': cliente.email})

if __name__ == '__main__':
    app.run(debug=True)