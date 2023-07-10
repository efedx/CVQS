FROM openjdk:19-alpine
LABEL maintainer = "efe"
# The fourth line copies the JAR file from the host machine's "target" directory into the Docker image
# and renames it as "project.jar"
ADD target/terminal-0.0.1-SNAPSHOT.jar terminal.jar
ENTRYPOINT ["java", "-jar", "terminal.jar"]