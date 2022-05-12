# BUILD
FROM maven as backend-b
COPY . .
RUN mvn -B clean package

# DEPLOY
FROM openjdk:17
COPY --from=backend-b target/*.jar backend-praxis.jar
ENV DB_HOST_IP = localhost
ENTRYPOINT ["java", "-jar", "backend-praxis.jar"]