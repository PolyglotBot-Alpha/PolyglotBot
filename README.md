# README

## Overview

This project is a microservices-based application composed of several services, each responsible for a specific functionality. The services communicate with each other using Kafka as a messaging broker, and they are containerized using Docker. The application also uses Zookeeper for managing the Kafka brokers and relies on MySQL and PostgreSQL for data persistence.

## Tools
1. [Postman](https://www.postman.com/)
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/)
3. [Helm](https://helm.sh/)
4. [Skaffold](https://skaffold.dev/)
5. [K9s](https://k9scli.io/)
   
## Services

![UML Diagram](https://iyvhmpdfrnznxgyvvkvx.supabase.co/storage/v1/object/public/articles/whiteboard_exported_image.png "Micro-Service Structure")


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
