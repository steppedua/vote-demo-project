FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=build/libs/vote-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} vote-service.jar
ENTRYPOINT ["java","-jar","vote-service.jar"]