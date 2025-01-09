from flask import Flask, request, jsonify
from calc_risk import calculate_risk
import numpy as np

app = Flask(__name__)

@app.route('/', methods=['GET'])
def home():
    return jsonify({'message': 'Welcome to the Risk Prediction API!'})

@app.route('/calculate-risk', methods=['POST'])
def predict():
    try:
        # Get data from request
        data = request.get_json()

        # Validate input data
        if not data or 'input_sequence' not in data:
            return jsonify({'error': 'No input_sequence provided'}), 400

        input_sequence = data['input_sequence']

        # Validate input sequence shape and type
        if not isinstance(input_sequence, list) or len(input_sequence) != 5:
            return jsonify({'error': 'Input sequence must be a list of 5 time steps'}), 400

        for step in input_sequence:
            if not isinstance(step, list) or len(step) != 32:
                return jsonify({'error': 'Each time step must contain 32 parameters'}), 400

        # Convert input to correct format
        input_array = np.array(input_sequence)

        # Calculate risk
        risk_score = calculate_risk(input_array)

        return jsonify({
            'risk_score': risk_score
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)