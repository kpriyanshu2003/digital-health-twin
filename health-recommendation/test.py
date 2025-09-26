import requests
import json

# Define the URL for the endpoint
URL = "http://127.0.0.1:5000/get_recommendations"

# Sample data to send as JSON
sample_data = {
    "vitals": {
        "heart_rate": 110,
        "blood_pressure": [150, 95],
        "spo2": 93,
        "respiratory_rate": 22
    },
    "body_params": {
        "age": 55,
        "gender": "M",
        "bmi": 31.2,
        "weight": 95,
        "height": 175
    }
}


def test_get_recommendations():
    try:
        # Make a POST request to the endpoint
        response = requests.post(URL, json=sample_data)

        # Check if the request was successful
        if response.status_code == 200:
            print("Response from server:")
            print(json.dumps(response.json(), indent=4))
        else:
            print(f"Failed with status code {response.status_code}:")
            print(response.text)
    except Exception as e:
        print(f"An error occurred: {str(e)}")


if __name__ == "__main__":
    test_get_recommendations()