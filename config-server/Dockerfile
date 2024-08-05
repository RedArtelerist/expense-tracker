# Stage 1: Build the project
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /config-server

# Copy the pom.xml and source code
COPY config-server/pom.xml .
COPY config-server/src ./src

# Build the application
RUN mvn -B clean package -DskipTests -Dcheckstyle.skip

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
RUN apk --no-cache add bash curl

# Copy the built JAR file from the build stage
COPY --from=build /config-server/target/*.jar /app/app.jar

# Set JAVA_TOOL_OPTIONS to use the OpenTelemetry Java agent
#ENV JAVA_TOOL_OPTIONS="-javaagent:/app/libs/opentelemetry-javaagent-1.32.0.jar"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]