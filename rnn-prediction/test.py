import requests
import json
import sys

def test_risk_calculation():
    # Test data
    sample_input = [
        [120, 1.0, 150, 140, 4.0, 100, 5.5, 200, 1.2, 0.9, 90, 4.5, 70, 1.1, 2.0, 3.0, 1.8, 0.6, 12, 50, 30, 70, 1.0,
         1.2, 9.5, 25, 20, 3.2, 2.5, 15, 1.0, 3.1],
        [125, 1.1, 160, 138, 4.2, 105, 5.6, 210, 1.3, 0.8, 85, 4.6, 75, 1.2, 2.1, 3.1, 1.7, 0.7, 13, 52, 31, 72, 1.1,
         1.3, 9.6, 26, 21, 3.3, 2.4, 16, 1.1, 3.2],
        [122, 1.2, 155, 137, 4.1, 102, 5.4, 205, 1.1, 0.7, 88, 4.4, 72, 1.0, 2.2, 3.2, 1.9, 0.5, 11, 51, 29, 73, 1.0,
         1.1, 9.4, 24, 22, 3.4, 2.6, 14, 1.2, 3.3],
        [123, 1.3, 158, 139, 4.3, 103, 5.7, 208, 1.4, 0.6, 87, 4.3, 73, 1.3, 2.3, 3.0, 1.6, 0.8, 14, 53, 30, 74, 1.2,
         1.4, 9.7, 27, 23, 3.5, 2.7, 17, 1.3, 3.4],
        [124, 1.4, 157, 136, 4.4, 104, 5.8, 207, 1.5, 0.5, 86, 4.2, 74, 1.4, 2.4, 3.3, 1.5, 0.9, 15, 54, 31, 75, 1.3,
         1.5, 9.8, 28, 24, 3.6, 2.8, 18, 1.4, 3.5]
    ]

    # API endpoint
    url = 'http://localhost:5000/calculate-risk'

    # Prepare request payload
    payload = {
        'input_sequence': sample_input
    }

    try:
        # Make POST request
        response = requests.post(url, json=payload)

        # Check if request was successful
        response.raise_for_status()

        # Parse response
        result = response.json()

        print("Test successful!")
        print(f"Risk score: {result['risk_score']}")

    except requests.exceptions.ConnectionError:
        print("Error: Could not connect to the server. Make sure the Flask server is running.")
        sys.exit(1)
    except requests.exceptions.HTTPError as e:
        print(f"HTTP Error: {e}")
        print(f"Response: {response.text}")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {str(e)}")
        sys.exit(1)


if __name__ == "__main__":
    test_risk_calculation()