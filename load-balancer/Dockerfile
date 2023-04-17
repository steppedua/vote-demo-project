FROM openjdk:17-jdk-slim-buster
EXPOSE 8090
ARG JAR_FILE=build/libs/load-balancer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} load-balancer.jar
ENTRYPOINT ["java","-jar","load-balancer.jar"]