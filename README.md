# README

## Overview

This project is a microservices-based application composed of several services, each responsible for a specific functionality. The services communicate with each other using Kafka as a messaging broker, and they are containerized using Docker. The application also uses Zookeeper for managing the Kafka brokers and relies on MySQL and PostgreSQL for data persistence.

## Services

1. **Zookeeper**
   - Image: `confluentinc/cp-zookeeper:latest`
   - Manages and coordinates Kafka brokers.
   - Exposes port `2181`.

2. **Kafka**
   - Image: `confluentinc/cp-kafka:latest`
   - Message broker for communication between microservices.
   - Exposes port `9092`.
   - Depends on Zookeeper.

3. **MySQL Database**
   - Image: `mysql:latest`
   - Stores data for services that require MySQL.
   - Exposes port `3306`.
   - Data is persisted in the Docker volume `mysql-data`.

4. **PostgreSQL Database**
   - Image: `postgres:latest`
   - Stores data for services that require PostgreSQL.
   - Exposes port `5432`.
   - Data is persisted in the Docker volume `postgres-data`.

5. **Chat Service**
   - Location: `./chat-service`
   - Manages chat functionalities.
   - Uses PostgreSQL as its database.
   - Communicates with other services via Kafka.
   - Exposes port `8085`.

6. **Recommendation Service**
   - Location: `./recommendation-service`
   - Provides recommendations based on data.
   - Uses MySQL as its database.
   - Communicates with other services via Kafka.
   - Exposes port `8083`.

7. **Subscription Service**
   - Location: `./subscription-service`
   - Manages subscription plans and payments.
   - Uses MySQL as its database.
   - Communicates with other services via Kafka.
   - Exposes port `8082`.
   - Requires a Stripe API key for payment processing.

8. **User Service**
   - Location: `./user-service`
   - Manages user-related functionalities.
   - Uses MySQL as its database.
   - Communicates with other services via Kafka.
   - Exposes port `8081`.

9. **Gateway**
   - Location: `./gateway`
   - Acts as the entry point for the application, routing requests to the appropriate services.
   - Exposes port `8080`.

## Network

All services are connected through a Docker network named `backend`. This network allows the services to communicate with each other internally by their service names.

## Volumes

- `mysql-data`: Persists MySQL database data.
- `postgres-data`: Persists PostgreSQL database data.

## How to Run the Application

1. **Install Docker and Docker Compose:**
   Ensure that you have Docker and Docker Compose installed on your machine.

2. Import OpenAI API key to to env variables. 
3. **Build and Start the Services:**
   Navigate to the directory containing the `docker-compose.yml` file and run the following command:
   ```sh
   docker-compose up --build
   ```
This command will build the Docker images for the services and start them.

## Access the Application

Once the services are up and running, you can access the gateway at [http://localhost:8080](http://localhost:8080).

## Environment Variables

Each service requires certain environment variables for configuration:

### Chat Service:
- `SPRING_PROFILES_ACTIVE`: Spring profile to use.
- `SPRING_DATASOURCE_URL`: URL to connect to PostgreSQL.
- `SPRING_DATASOURCE_USERNAME`: Username for PostgreSQL.
- `SPRING_DATASOURCE_PASSWORD`: Password for PostgreSQL.
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address.

### Recommendation, Subscription, and User Services:
- `SPRING_PROFILES_ACTIVE`: Spring profile to use.
- `SPRING_DATASOURCE_URL`: URL to connect to MySQL.
- `SPRING_DATASOURCE_USERNAME`: Username for MySQL.
- `SPRING_DATASOURCE_PASSWORD`: Password for MySQL.
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address.

### Subscription Service Only:
- `STRIPE_API_KEY`: API key for Stripe payment processing.

### Gateway:
- `SPRING_PROFILES_ACTIVE`: Spring profile to use.
- Service URIs to route requests.

## Stopping the Application

To stop the running services, press `Ctrl + C` in the terminal where the services are running or execute:

```sh
docker-compose down
```

## Cleaning Up

If you wish to remove the containers along with the volumes, use:

```sh
docker-compose down -v
```

## Troubleshooting
```sh
docker-compose logs <service-name>
```
