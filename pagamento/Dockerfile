FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/pagamento*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app.jar"]

EXPOSE 8080

RUN echo 'Iniciando aplicacao...'