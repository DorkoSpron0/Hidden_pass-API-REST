FROM amazoncorretto:21-al2023

WORKDIR /app

COPY target/hidden_pass-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]