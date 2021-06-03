#!/bin/bash

# exit when any command fails
set -e

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# build frontend
cd ../../software/frontend/
docker build -t ucm-frontend . --build-arg frontend_production=true --build-arg backend_url="${DEPLOYMENT_URL}/api" \
--build-arg backend_jwt_client_id=${UCM_BACKEND_JWT_CLIENT_ID} \
--build-arg backend_jwt_client_secret=${UCM_BACKEND_JWT_CLIENT_SECRET}
cd ../../deployment/local-production/

# build backend
cd ../../software/backend/
mvn clean package -DskipTests
docker build -t ucm-backend .
cd ../../deployment/local-production/

# run compose
docker-compose up -d
