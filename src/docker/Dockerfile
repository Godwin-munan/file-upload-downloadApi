FROM openjdk:17-alpine

WORKDIR /app

COPY target/file-upload-downloadApi-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]
