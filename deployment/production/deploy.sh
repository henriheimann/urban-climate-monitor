#!/bin/bash

# exit when any command fails
set -e

# default variable values
SKIP_FRONTEND_BUILD=0
SKIP_BACKEND_BUILD=0

# parse opts
while getopts sb option
do
    case "${option}" in
        s) SKIP_FRONTEND_BUILD=1;;
        b) SKIP_BACKEND_BUILD=1;;
    esac
done

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# make sure directory exists on deployment server
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'mkdir -p ~/ucm'

# build and upload frontend if not skipped
if [ "${SKIP_FRONTEND_BUILD}" = "0" ]; then
    cd ../../software/frontend/
    docker build -t ucm-frontend . --build-arg frontend_production=true --build-arg backend_url="${DEPLOYMENT_URL}/api" \
    --build-arg backend_jwt_client_id=${UCM_BACKEND_JWT_CLIENT_ID} \
    --build-arg backend_jwt_client_secret=${UCM_BACKEND_JWT_CLIENT_SECRET}
    docker save ucm-frontend > ucm-frontend.tar
    scp ucm-frontend.tar "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}:~/ucm"
    rm ucm-frontend.tar
    cd ../../deployment/production/
else
    echo "Skipping frontend build..."
fi

# build and upload backend if not skipped
if [ "${SKIP_BACKEND_BUILD}" = "0" ]; then
    cd ../../software/backend/
    mvn clean package -DskipTests
    docker build -t ucm-backend .
    docker save ucm-backend > ucm-backend.tar
    scp ucm-backend.tar "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}:~/ucm"
    rm ucm-backend.tar
    cd ../../deployment/production/
else
    echo "Skipping backend build..."
fi

# upload compose and configuration
rsync -aP docker-compose.yml telegraf.conf .env nginx grafana-provisioning ssl_renew.sh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}:~/ucm"

# load frontend
if [ "${SKIP_FRONTEND_BUILD}" = "0" ]; then
    ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'cd ~/ucm && docker load < ucm-frontend.tar && rm ucm-frontend.tar'
fi

# load backend
if [ "${SKIP_BACKEND_BUILD}" = "0" ]; then
    ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'cd ~/ucm && docker load < ucm-backend.tar && rm ucm-backend.tar'
fi

# create dh keys if they don't exist
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'if [ ! -d ~/ucm/dhparam ]; then mkdir -p ~/ucm/dhparam && openssl dhparam -out ~/ucm/dhparam/dhparam-2048.pem 2048; fi'

# run compose
ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" 'cd ~/ucm && docker-compose up -d && docker image prune -f'

# add crontab if not exits
grep_result=$(ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" "crontab -l | grep '~/ucm/ssl_renew.sh'" 2>&1) || true
if [ -z "${grep_result}" ]; then
    echo "creating crontab entry..."
    ssh "${DEPLOYMENT_USER}@${DEPLOYMENT_HOST}" '(crontab -l ; echo "0 12 * * * ~/ucm/ssl_renew.sh >> ~/ucm/ssl_renew.log 2>&1") | crontab -'
fi
