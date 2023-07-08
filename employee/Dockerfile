# define base docker image
FROM openjdk:19-alpine
LABEL maintainer = "efe"
EXPOSE 8080
# The fourth line copies the JAR file from the host machine's "target" directory into the Docker image
# and renames it as "project.jar"
ADD target/employeeTerminalContainer-0.0.1-SNAPSHOT.jar employeeTerminalContainer.jar
ENTRYPOINT ["java", "-jar", "employeeTerminalContainer.jar"]