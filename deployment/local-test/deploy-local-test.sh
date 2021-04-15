#!/bin/bash

# exit when any command fails
set -e

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# build frontend
cd ../../software/frontend/
docker build -t ucm-frontend .
cd ../../deployment/local-test/

# build backend
cd ../../software/backend/
mvn clean package -DskipTests
docker build -t ucm-backend .
cd ../../deployment/local-test/

# run compose
docker-compose up -d
