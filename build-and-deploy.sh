#!/bin/bash

echo "Building Key Generator Service..."
mvn clean package -f ./keygenerator/pom.xml

echo "Building API..."
mvn clean package -f ./api/pom.xml

echo "Starting containers with Docker Compose..."
docker-compose build && docker-compose up -d

echo "\n\n\n\n\n\n\n Access Swagger UI from: http://localhost:8080/swagger-ui.html \n\n\n\n\n\n"