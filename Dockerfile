FROM maven:3-openjdk-16 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:16-jdk-slim
COPY --from=build /target/major-0.0.1-SNAPSHOT.jar major.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","major.jar"]

