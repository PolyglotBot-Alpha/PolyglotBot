# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

LABEL authors="yuqi.guo17@gmail.com"

# Set the working directory in the container
WORKDIR /app

# Copy the jar file to the container
COPY target/subscription-service.jar /app/app.jar

# Make port 8082 available to the world outside this container
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
