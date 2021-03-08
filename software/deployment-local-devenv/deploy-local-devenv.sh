#!/bin/bash

# exit when any command fails
set -e

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# run compose
docker-compose up -d
