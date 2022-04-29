CREAR RED DOCKER
docker network ls
docker network create praxis-network --subnet=10.11.0.0/16

CREAR CONTENEDOR BD
docker run --name my-postgres --network="praxis-network" --ip 10.11.0.2 -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres

CREAR CONTENEDOR BACKEND
docker build -t backend .
docker run -d -e DB_HOST_IP=10.11.0.2 --name my-backend-praxis --network="praxis-network" --ip 10.11.0.3 -p 8080:8080 backend

CREAR CONTENEDOR FRONTEND
docker build -t frontend .
docker run -d --name my-frontend-praxis --network="praxis-network" --ip 10.11.0.4 -p 80:4200 frontend

Conocer IP de la base de datos (No quemada)
docker ps
docker inspect --format= "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" (cont id)

DOCKER HUB LINK
https://hub.docker.com/u/santiagot1105
