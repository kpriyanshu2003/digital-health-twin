import requests

url = 'http://localhost:5000/process-prescription'
files = {'file': open('res/image3.png', 'rb')}
response = requests.post(url, files=files)
print(response.json())