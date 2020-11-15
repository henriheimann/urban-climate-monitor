### Make sure to fix ufw with docker
`sudo nano /etc/default/docker`, add `DOCKER_OPTS="--iptables=false"` and restart docker daemon via `sudo systemctl restart docker`

### Create an .env file including the following content
```
# InfluxDB
INFLUXDB_HOST=influxdb
INFLUXDB_PORT=8086
INFLUXDB_DB=<InfluxDB database name>
INFLUXDB_ADMIN_USER=<InfluxDB admin username>
INFLUXDB_ADMIN_PASSWORD=<InfluxDB admin password>
INFLUXDB_USER=<InfluxDB username>
INFLUXDB_USER_PASSWORD=<InfluxDB user password>

# Telegraf
TELEGRAF_HOST=telegraf

# Grafana
GF_HOST=grafana
GF_PORT=3000
GF_SECURITY_ADMIN_USER=<Grafana admin username>
GF_SECURITY_ADMIN_PASSWORD=<Grafana admin password>

# The Things Network
TTN_REGION=<TTN region>
TTN_APP_ID=<TTN application id>
TTN_APP_ACCESS_KEY=<TTN application access key>

# Urban Climate Monitor Frontend
UCM_FRONTEND_HOST=ucm-frontend
UCM_FRONTEND_PORT=80

# Deployment Info
DEPLOYMENT_USER=<User on deployment destination>
DEPLOYMENT_HOST=<IP / URL of deployment destination>

# Let's Encrypt Certification
LETS_ENCRYPT_MAIL=<Email for Let's Encrypt>
```