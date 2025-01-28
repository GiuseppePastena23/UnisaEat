from flask import Flask, jsonify, request, render_template
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.exc import SQLAlchemyError
import random
import string
import asyncio
import threading

# Configurazione dell'app Flask
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://android:android@localhost/mensadb'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Inizializzazione di SQLAlchemy
db = SQLAlchemy(app)

# Definizione dei modelli ORM
class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    cf = db.Column(db.String(16), unique=True)
    name = db.Column(db.String(50))
    surname = db.Column(db.String(50))
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(255))
    status = db.Column(db.String(50))
    phone = db.Column(db.String(20))
    credit = db.Column(db.Float, default=0.0)
    

class Transazione(db.Model):
    __tablename__ = 'transazioni'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    amount = db.Column(db.Float, nullable=False)
    datetime = db.Column(db.DateTime)
    mode = db.Column(db.String(50))

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    #password = dat.get('password')
    user = User.query.filter_by(email=email).first()
    
    # check password

    if user:
        user_data = {
            'id': user.id,
            'cf': user.cf,
            'name': user.name,
            'surname': user.surname,
            'email': user.email,
            'status': user.status,
            'phone': user.phone,
            'credit': user.credit
        }
        return jsonify({'user': user_data})
    else:
        return jsonify({'status': 'failure', 'message': 'Invalid credentials'}), 400

def run_flask():
    app.run(host="0.0.0.0")

if __name__ == "__main__":
    with app.app_context():
        db.create_all()
 
    flask_thread = threading.Thread(target=run_flask)
    flask_thread.start()
