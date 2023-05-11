# define base docker image
FROM openjdk:17-alpine
LABEL maintainer = "efe"
EXPOSE 8080
# The fourth line copies the JAR file from the host machine's "target" directory into the Docker image
# and renames it as "project.jar"
ADD target/project-0.0.1-SNAPSHOT.jar toyotaproject.jar
ENTRYPOINT ["java", "-jar", "toyotaproject.jar"]