#!/bin/bash

# build frontend
cd ../Frontend/
docker build -t ucm-frontend . || exit

# compose up
cd ../Deployment/
docker-compose up
