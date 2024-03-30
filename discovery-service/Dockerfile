FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
RUN mkdir -p /eureka
WORKDIR /eureka
COPY /discovery-service/pom.xml /eureka
COPY /discovery-service/src /eureka/src
RUN mvn -B -f /eureka/pom.xml clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:21-jre-alpine
RUN apk --no-cache add bash curl
COPY --from=build /eureka/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]
