Weather Data Service API

---

## Setup & Run Run Locally
```bash
cd docker;
docker compose build;
docker compose up;
```
### Base URL
```bash
http://localhost:8080/api/v1
```
# Endpoints
### POST /sensors
Register a new sensor.
```
Example Request
curl --location 'http://localhost:8080/api/v1/sensors' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Bonham-1",
    "location": "Galway"
}'
Responses
Status    	Description
201         Created	Sensor successfully registered
409         Conflict	Sensor name already exists
```

### Example Response
```bash
{
    "name": "Bonham-4",
    "location": "Galway",
    "createdAt": "2025-10-26T13:31:20.993667Z"
}
```

### GET /sensors
Retrieve all registered sensors.

```bash
curl --location 'http://localhost:8080/api/v1/sensors'
Example Response — 200 OK
[
    {
        "name": "sensor-a",
        "location": "roof",
        "createdAt": "2025-10-24T18:32:09.026996Z"
    },
    {
        "name": "sensor-b",
        "location": "lobby",
        "createdAt": "2025-10-24T18:36:42.713253Z"
    }
]
```
### POST /readings
Register a new sensor reading.
```bash

curl --location 'http://localhost:8080/api/v1/readings' \
--header 'Content-Type: application/json' \
--data '{
    "sensorName": "sensor-a",
    "metricType": "HUMIDITY",
    "value": "12"
}'
Responses
Status	    Description
201         Created	Reading successfully created
404         Not Found	Sensor name does not exist
```
## Example Response
```bash

{
    "id": 12,
    "sensor": {
        "id": 1,
        "name": "sensor-a",
        "location": "roof",
        "createdAt": "2025-10-24T18:32:09.026996Z"
    },
    "timestamp": "2025-10-26T12:38:41.132422123Z",
    "metricType": "HUMIDITY",
    "value": 12.0
}
```

### GET /readings
Retrieve all recorded readings.

```bash
curl --location 'http://localhost:8080/api/v1/readings'
Example Response — 200 OK
[
    {
        "id": 1,
        "sensor": {
            "id": 1,
            "name": "sensor-a",
            "location": "roof",
            "createdAt": "2025-10-24T18:32:09.026996Z"
        },
        "timestamp": "2025-10-24T18:39:48.308216Z",
        "metricType": "TEMPERATURE",
        "value": 20.0
    },
    {
        "id": 2,
        "sensor": {
            "id": 1,
            "name": "sensor-a",
            "location": "roof",
            "createdAt": "2025-10-24T18:32:09.026996Z"
        },
        "timestamp": "2025-10-24T18:39:57.376880Z",
        "metricType": "TEMPERATURE",
        "value": 22.0
    }
]
```
### GET /readings/aggregate
Retrieve aggregated readings for one or more sensors.

supports filters via query params

###Example Request
```bash
curl --location 'http://localhost:8080/api/v1/readings/aggregate?sensors=sensor-a&aggregationType=MIN&metrics=TEMPERATURE%2CHUMIDITY'
Query Parameters
Name	          Type	                Required	            Description
sensors	        list(string)	        No (Default=ALL)      One or more sensor names to include (omission = ALL)
metrics	        list(MetricType.enum)	no (Default=ALL)	    Metrics to aggregate (e.g. TEMPERATURE,HUMIDITY)
aggregationType	enum	                No	                  AVG, MIN, or MAX (default AVG)
start	          ISO datetime	        No	                  Start time (default: 7 days ago)
end	            ISO datetime	        No	                  End time (default: now)
```

### Responses
```bash
Status	Description
200 OK	Aggregated data returned successfully
404 Not Found	No valid sensors found
```
### Example Response
```bash
[
    {
        "metricType": "HUMIDITY",
        "aggregationType": "MIN",
        "value": 12.0,
        "count": 5,
        "startTime": "2025-10-19T12:42:07.074153068Z",
        "endTime": "2025-10-26T12:42:07.074153068Z"
    },
    {
        "metricType": "TEMPERATURE",
        "aggregationType": "MIN",
        "value": 20.0,
        "count": 2,
        "startTime": "2025-10-19T12:42:07.074153068Z",
        "endTime": "2025-10-26T12:42:07.074153068Z"
    }
]
```
