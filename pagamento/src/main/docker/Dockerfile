FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY @project.build.finalName@.jar /app

EXPOSE 8080

RUN echo 'Iniciando aplicação...' 
CMD ["java", "-jar", "/app/@project.build.finalName@.jar"]