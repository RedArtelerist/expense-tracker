FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
RUN mkdir -p /config-server
WORKDIR /config-server
COPY /config-server/pom.xml /config-server
COPY /config-server/src /config-server/src
RUN mvn -B -f /config-server/pom.xml clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:21-jre-alpine
RUN apk --no-cache add bash curl
COPY --from=build /config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","app.jar"]
