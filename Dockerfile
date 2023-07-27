FROM maven:3.8.6-openjdk-11 as build
WORKDIR /build
COPY . .
EXPOSE 3306
RUN mvn clean package

FROM openjdk:11
WORKDIR /app
COPY --from=build ./build/target/*.jar ./user-api.jar
EXPOSE 8080
ENTRYPOINT java -jar user-api.jar