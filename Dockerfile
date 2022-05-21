# BUILD
FROM maven as backend-b
COPY . .
ARG DB_HOST_IP
ARG DB_USERNAME
ARG DB_PASSWORD
RUN mvn -B clean package

# DEPLOY
FROM openjdk:17
COPY --from=backend-b target/*.jar backend-praxis.jar
ENV DB_HOST_IP = localhost
ENV DB_USERNAME = $DB_USERNAME
ENV DB_PASSWORD = $DB_PASSWORD
ENTRYPOINT ["java", "-jar", "backend-praxis.jar"]