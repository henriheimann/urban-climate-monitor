#!/bin/bash

# exit when any command fails
set -e

# default variable values
SKIP_FRONTEND_BUILD=0

# parse opts
while getopts s option
do
    case "${option}" in
        s) SKIP_FRONTEND_BUILD=1;;
    esac
done

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# make sure directory exists on deployment server
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'mkdir -p ~/ucm'

# build and upload frontend if not skipped
if [ "${SKIP_FRONTEND_BUILD}" = "0" ]; then
    cd ../Frontend/
    docker build -t ucm-frontend .
    docker save ucm-frontend > ucm-frontend.tar
    scp ucm-frontend.tar "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}:~/ucm"
    rm ucm-frontend.tar
else
    echo "Skipping frontend build..."
fi

# upload compose and configuration
cd ../Deployment/
rsync -aP docker-compose.yml telegraf.conf .env nginx grafana-provisioning "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}:~/ucm"

# load frontend
if [ "${SKIP_FRONTEND_BUILD}" = "0" ]; then
    ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'cd ~/ucm && docker load < ucm-frontend.tar && rm ucm-frontend.tar'
fi

# create dh keys if they don't exist
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'if [ ! -d ~/ucm/dhparam ]; then mkdir -p ~/ucm/dhparam && openssl dhparam -out ~/ucm/dhparam/dhparam-2048.pem 2048; fi'

# run compose
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'cd ~/ucm && docker-compose up -d && docker image prune -f'
