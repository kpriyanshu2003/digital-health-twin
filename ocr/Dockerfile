FROM python:3.12-slim  

# Install OpenCV dependencies
RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Install Python dependencies
COPY requirements.txt .
RUN pip install -r requirements.txt

# Add your application code
COPY . /app
WORKDIR /app

CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:5000", "wsgi:application"]
