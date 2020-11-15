#!/bin/bash

cd ~/ucm
docker-compose run certbot renew && docker-compose kill -s SIGHUP reverse-proxy
docker system prune -af
