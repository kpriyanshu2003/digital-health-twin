from flask import Flask, request, jsonify
import gen  # Importing the gen.py file

app = Flask(__name__)


@app.route('/get_recommendations', methods=['POST'])
def get_recommendations():
    try:
        # Parse the incoming JSON data
        data = request.get_json()
        if not data:
            return jsonify({"error": "Invalid input: No JSON data provided"}), 400

        # Extract vitals and body parameters
        vitals = data.get("vitals")
        body_params = data.get("body_params")
        if not vitals or not body_params:
            return jsonify({"error": "Invalid input: Missing 'vitals' or 'body_params'"}), 400

        # Call the function from gen.py
        response = gen.get_recom(vitals, body_params)
        return response

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)