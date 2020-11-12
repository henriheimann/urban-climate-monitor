### Make sure to fix ufw with docker
`sudo nano /etc/default/docker` and add `DOCKER_OPTS="--iptables=false`

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
GRAFANA_HOST=grafana
GRAFANA_PORT=3000
GRAFANA_USER=<Grafana admin username>
GRAFANA_PASSWORD=<Grafana admin password>

# The Things Network
TTN_REGION=<TTN region>
TTN_APP_ID=<TTN application id>
TTN_APP_ACCESS_KEY=<TTN application access key>
```