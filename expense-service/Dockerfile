FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

RUN mkdir -p /dto
WORKDIR /dto
COPY /dto/pom.xml /dto
COPY /dto/src /dto/src
RUN mvn -B -f /dto/pom.xml clean install -DskipTests -Dcheckstyle.skip

RUN mkdir -p /expense
WORKDIR /expense
COPY /expense-service/pom.xml /expense
COPY /expense-service/src /expense/src
RUN mvn -B -f /expense/pom.xml clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:21-jre-alpine
RUN apk --no-cache add bash curl
COPY --from=build /expense/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
