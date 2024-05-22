FROM openjdk:21
LABEL authors="MMA" \
      description="This is a simple Spring Boot application for managing a list of contacts"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]