FROM maven:3.8.5-openjdk-17-slim AS build
COPY . /app
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:17
COPY --from=build /app/h5WebRest/target/*.jar homework5-1.0.0.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "homework5-1.0.0.jar"]