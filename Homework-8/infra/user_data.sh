#!/bin/bash
## Install Java
sudo apt update && sudo apt install openjdk-8-jdk -y

## Install Jenkins
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -

sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > \
    /etc/apt/sources.list.d/jenkins.list'

sudo apt-get update && sudo apt-get install jenkins -y

## Eneable and start Jenkins Service
sudo systemctl enable jenkins
sudo systemctl start jenkins

## Install docker
sudo apt install docker.io -y

## Install docker compose
sudo curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

sudo groupadd docker
sudo usermod -aG docker jenkins
sudo usermod -aG docker ubuntu
sudo newgrp docker

## Restart Jenkins to load docker access
sudo service jenkins restart