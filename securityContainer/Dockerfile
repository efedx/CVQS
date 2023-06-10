FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR .
#COPY pom.xml /securityContainer
#COPY . /securityContainer
#COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19-alpine
LABEL maintainer = "efe"
EXPOSE 8080
# The fourth line copies the JAR file from the host machine's "target" directory into the Docker image
# and renames it as "project.jar"
ADD target/securityContainer-0.0.1-SNAPSHOT.jar securityContainer.jar
ENTRYPOINT ["java", "-jar", "securityContainer.jar"]