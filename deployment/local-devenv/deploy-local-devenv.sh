#!/bin/bash

# exit when any command fails
set -e

# source .env file
source <(grep -v -e '^#\|^[[:space:]]*$' .env | sed -e 's/\r$//' -e 's/^/export /' -e 's/=/="/' -e 's/$/"/')

# run compose
docker-compose up -d

sleep 5

# import influxdb testdata
docker cp influxdb-testdata ucm-influxdb:/tmp/testdata
docker exec -t ucm-influxdb bash -c "influx -username ${INFLUXDB_ADMIN_USER} -password ${INFLUXDB_ADMIN_PASSWORD} -database ${INFLUXDB_DB} -execute 'DROP DATABASE ${INFLUXDB_DB}' && influxd restore -portable -db ${INFLUXDB_DB} /tmp/testdata"
