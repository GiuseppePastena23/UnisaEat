from flask import Flask, jsonify, request, render_template
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import and_, func, desc
from datetime import datetime
import random
import string
import asyncio
import time
import threading
import os
import base64
import hashlib


# Configurazione dell'app Flask
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://android:android@localhost/mensadb'
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
    token = db.Column(db.String(32))
    

class Transaction(db.Model):
    __tablename__ = 'transactions'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    amount = db.Column(db.Float, nullable=False)
    datetime = db.Column(db.DateTime)
    mode = db.Column(db.String(50))

    def to_dict(self):
        return {
            "id": self.id,
            "user_id": self.user_id,
            "amount": self.amount,
            "datetime": self.datetime.strftime('%Y-%m-%d %H:%M:%S'),  # Formattazione della data
            "mode": self.mode
        }
    
############################################

def update_token(id):
    user = User.query.get(id)
    if (user.token is None):
        user.token = "none"
    print("Updating token from " + user.token)
    token = ''.join(random.choices(string.ascii_letters + string.digits, k=32))
    user.token = token
    db.session.commit()
    print("Updated token to " + user.token)

@app.route('/getToken', methods=['GET'])
def get_token():
    user_id = request.args.get('user_id', type=int)
    if user_id is None:
        return jsonify({'error': 'user_id parameter is required'}), 400
    try:
        user = User.query.get(user_id)
        if user:
            
            return jsonify({'token': user.token}), 200
        else:
            return jsonify({'message': 'User not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')
    user = User.query.filter_by(email=email).first()

    if user and user.password == password:
        update_token(user.id)
        user_data = {
            'id': user.id,
            'cf': user.cf,
            'name': user.name,
            'surname': user.surname,
            'email': user.email,
            'password': user.password,
            'status': user.status,
            'phone': user.phone,
            'credit': user.credit,
            'token': user.token
        }
        return jsonify(user_data)
    else:
        return jsonify({'status': 'failure', 'message': 'Invalid credentials'}), 400


def hash_password(password: str) -> str:
    
    return hashlib.sha256(password.encode('utf-8')).hexdigest()  

@app.route('/orders', methods=['GET'])
def todayOrders():
    today = datetime.now().date() 

    orders = Transaction.query.filter(
        func.date(Transaction.datetime) == today, 
        Transaction.mode.ilike('order;%')  
    ).order_by(desc(Transaction.id)).all()

    
    return jsonify([order.to_dict() for order in orders])



@app.route('/getUserTransactions', methods=['GET'])
def get_user_transaction():
    user_id = request.args.get('user_id', type=int)
    
    if user_id is None:
        return jsonify({'error': 'user_id parameter is required'}), 400

    try:
        # Query per recuperare tutte le transazioni relative all'utente specifico
        transactions = Transaction.query.filter_by(user_id=user_id).order_by(desc(Transaction.id)).all()

        if transactions:
            return jsonify([transaction.to_dict() for transaction in transactions]), 200
        else:
            return jsonify([]), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/doTransaction', methods=['POST'])
def do_transaction():
    data = request.get_json()
    user_id = data.get('user_id')
    amount = data.get('amount')
    mode = data.get('mode')

    if user_id is None or amount is None or mode is None:
        return jsonify({'error': 'user_id, amount and mode parameters are required'}), 400

    try:
        transaction = Transaction(user_id=user_id, amount=amount, datetime=datetime.now(), mode=mode)
        db.session.add(transaction)
        db.session.commit()
        if mode.endswith('payment'):
            update_token(user_id)
        return 'Transaction completed successfully', 200
    except Exception as e:
        if str(e).startswith('(pymysql.err.OperationalError) (1644, \'Transaction failed: Not enough credit\')'):
            return 'Not enough credit', 400
        return str(e), 500
    

    

@app.route('/user', methods=['GET'])
def get_user_by_id():
    user_id = request.args.get('user_id', type=int)
    if user_id is None:
        return jsonify({'error': 'user_id parameter is required'}), 400

    try:
        user = User.query.get(user_id)
        if user:
            return jsonify({
                'id': user.id,
                'cf': user.cf,
                'name': user.name,
                'surname': user.surname,
                'email': user.email,
                'password': user.password,
                'status': user.status,
                'phone': user.phone,
                'credit': user.credit,
                'token': user.token
            }), 200
        else:
            return jsonify({'message': 'User not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/getDay', methods=['GET'])
def get_day():
    day = datetime.now().weekday()  
    now = datetime.now()
    return jsonify({'day': day, 'hour': now.hour, 'minute': now.minute}), 200




# Percorso della cartella delle immagini
IMAGE_FOLDER = "menu"

def image_to_base64(image_path):
    """Converte un'immagine in Base64."""
    try:
        with open(image_path, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
        return encoded_string
    except FileNotFoundError:
        return None

@app.route("/menu", methods=["GET"])
def get_menu_by_day():
    # Recupera il parametro 'day' dalla query string
    day = request.args.get("day", type=int)
    
    # Verifica se il giorno è compreso tra 0 e 4
    if day is None or not (0 <= day <= 4):
        return jsonify({"error": "Parametro 'day' deve essere compreso tra 0 e 4."}), 400

    # Percorso dell'immagine corrispondente
    image_path = os.path.join(IMAGE_FOLDER, f"{day}.jpg")

    # Converti immagine in Base64
    base64_image = image_to_base64(image_path)

    if base64_image is None:
        return jsonify({"error": "Immagine non trovata"}), 404

    # Risposta JSON con l'immagine codificata
    return "\"" + base64_image + "\"", 200


@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    cf = data.get('cf')
    name = data.get('name')
    surname = data.get('surname')
    email = data.get('email')
    password = data.get('password')
    status = data.get('status')
    phone = data.get('phone')

    if not cf or not name or not surname or not email or not password or not status or not phone:
        return jsonify({'error': 'All fields are required'}), 400

    try:
        user = User(cf=cf, name=name, surname=surname, email=email, password=password, status=status, phone=phone)
        db.session.add(user)
        db.session.commit()
        return 'User registered successfully', 200
    except Exception as e:
        return str(e), 500


def run_flask():
    app.run(host="0.0.0.0", threaded=True)

### MAIN ###
if __name__ == "__main__":
    with app.app_context():
        db.create_all()

    flask_thread = threading.Thread(target=run_flask)
    flask_thread.start()
