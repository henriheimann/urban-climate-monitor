### Make sure to fix ufw with docker
`sudo nano /etc/default/docker`, add `DOCKER_OPTS="--iptables=false"` and restart docker daemon via `sudo systemctl restart docker`

### Create an .env file including the following content
```
# InfluxDB
INFLUXDB_HOST=ucm-influxdb
INFLUXDB_PORT=8086
INFLUXDB_DB=<InfluxDB database name>
INFLUXDB_ADMIN_USER=<InfluxDB admin username>
INFLUXDB_ADMIN_PASSWORD=<InfluxDB admin password>
INFLUXDB_USER=<InfluxDB username>
INFLUXDB_USER_PASSWORD=<InfluxDB user password>

# Telegraf
TELEGRAF_HOST=ucm-telegraf

# Grafana
GF_HOST=ucm-grafana
GF_PORT=3000
GF_SECURITY_ADMIN_USER=<Grafana admin username>
GF_SECURITY_ADMIN_PASSWORD=<Grafana admin password>

# The Things Network
TTN_REGION=<TTN region>
TTN_APP_ID=<TTN application id>
TTN_APP_ACCESS_KEY=<TTN application access key>

# Postgres
POSTGRES_HOST=ucm-postgres
POSTGRES_PORT=5432
POSTGRES_DB=<Postgres database name>
POSTGRES_USER=<Postgres username>
POSTGRES_PASSWORD=<Postgres user password>

# Urban Climate Monitor Frontend
UCM_FRONTEND_HOST=ucm-frontend
UCM_FRONTEND_PORT=80

# Urban Climate Monitor Backend
UCM_BACKEND_HOST=ucm-backend
UCM_BACKEND_PORT=8080
UCM_BACKEND_JWT_CLIENT_ID=<JWT client id>
UCM_BACKEND_JWT_CLIENT_SECRET=<JWT client secret>
UCM_BACKEND_JWT_SIGNING_KEY=<JWT signing key>
UCM_BACKEND_ADMIN_EMAIL=<Backend admin email>
UCM_BACKEND_ADMIN_PASSWORD=<Backend admin passworc>

# Deployment Info
DEPLOYMENT_USER=<User on deployment destination>
DEPLOYMENT_HOST=<IP / URL of deployment destination>

# Let's Encrypt Certification
LETS_ENCRYPT_MAIL=<Email for Let's Encrypt>
```