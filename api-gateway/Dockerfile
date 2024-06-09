FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
RUN mkdir -p /gateway
WORKDIR /gateway
COPY /api-gateway/pom.xml /gateway
COPY /api-gateway/src /gateway/src
RUN mvn -B -f /gateway/pom.xml clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:21-jre-alpine
RUN apk --no-cache add bash curl
COPY --from=build /gateway/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
