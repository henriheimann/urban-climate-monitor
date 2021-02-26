#!/bin/bash

# exit when any command fails
set -e

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# build frontend
cd ../frontend/
docker build -t ucm-frontend .
cd ../deployment-local-test/

# run compose
docker-compose up -d