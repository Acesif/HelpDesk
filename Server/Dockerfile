# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copy the build files and wrapper
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Grant execute permission to the gradlew script
RUN chmod +x ./gradlew

# Copy the source code
COPY src ./src

# Build the application, creating the WAR file
RUN ./gradlew build --no-daemon

# Stage 2: Deploy the WAR on a Tomcat server
FROM tomcat:10.1-jdk17-temurin

# Remove the default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR from the build stage to Tomcat's webapps directory
COPY --from=build /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port Tomcat runs on
EXPOSE 8090

# Start Tomcat
CMD ["catalina.sh", "run"]

