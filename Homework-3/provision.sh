#!/usr/bin/env bash

apt-get update

echo "\n----- Installing Java 17 ------\n"
apt-get -y install openjdk-17-jdk

echo "\n----- Installing Maven ------\n"
wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
tar -xvf apache-maven-3.8.5-bin.tar.gz
cp -r apache-maven-3.8.5 /opt/maven

cat /etc/profile.d/maven.sh

echo 'export M2_HOME=/opt/maven' | tee -a /etc/profile.d/maven.sh
echo 'export MAVEN_HOME=/opt/maven' | tee -a /etc/profile.d/maven.sh
echo 'export PATH=${M2_HOME}/bin:${PATH}' | tee -a /etc/profile.d/maven.sh

chmod 755 /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh

echo "\n----- Installing Git ------\n"
apt-get -y install git

echo "\n----- Installing Docker ------\n"
apt-get install -y docker.io

echo "\n----- Run Docker ------\n"
sudo docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5433:5432 -d postgres

echo "\n----- Clone Repository ------\n"
git clone https://github.com/SantiagoT21/Praxis-Unal-Group4.git

echo "\n----- Run Aplication------\n"
cd Praxis-Unal-Group4
mvn spring-boot:run