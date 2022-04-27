FROM openjdk:17
COPY target/*.jar backend-praxis.jar
ENV DB_HOST_IP = localhost
ENTRYPOINT ["java", "-jar", "backend-praxis.jar"]