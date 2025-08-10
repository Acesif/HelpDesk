# Stage 1: Build the application with Gradle
FROM gradle:8.8-jdk17 AS build
WORKDIR /home/gradle/src
COPY . .
# The --no-daemon option is recommended for CI/CD environments
RUN gradle build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built executable WAR from the build stage
COPY --from=build /home/gradle/src/build/libs/*.war /app/helpdesk.war

# Expose the port the application will run on
EXPOSE 7878

# Set the command to run the application
# We also pass the server.port property to ensure Spring Boot uses the correct port
CMD ["java", "-jar", "/app/helpdesk.war", "--server.port=7878"]
