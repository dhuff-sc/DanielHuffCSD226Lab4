# Change this to match the version of JDK you are using for your server application
FROM amazoncorretto:21-alpine-jdk
COPY /target/DanielHuffCSD226Lab2Redo-0.0.1-SNAPSHOT.jar /app.jar
# Inform Docker that the container is listening on the specified port at runtime.
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
