#!/usr/bin/env bash

apt-get update

echo "\n----- Installing Java 17 ------\n"
apt-get --purge autoremove openjdk-17-jdk -y
apt-get -y install openjdk-17-jdk

echo "\n----- Installing Maven ------\n"

wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
tar -xvf apache-maven-3.8.5-bin.tar.gz
cp -r apache-maven-3.8.5 /opt/maven

FILE= /etc/profile.d/maven.sh

if [ -f $FILE ]
then
   echo "$FILE existe"
else
   cat $FILE
fi

if ! grep -qF 'export M2_HOME=/opt/maven' /etc/profile
    then
    echo 'export M2_HOME=/opt/maven' | tee -a /etc/profile.d/maven.sh
fi

if ! grep -qF 'export MAVEN_HOME=/opt/maven' /etc/profile
    then
    echo 'export MAVEN_HOME=/opt/maven' | tee -a /etc/profile.d/maven.sh
fi

if ! grep -qF 'export PATH=${M2_HOME}/bin:${PATH}' /etc/profile
    then
    echo 'export PATH=${M2_HOME}/bin:${PATH}' | tee -a /etc/profile.d/maven.sh
fi

chmod 755 /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh

echo "\n----- Installing Git ------\n"
apt-get --purge remove git -y
apt-get -y install git

echo "\n----- Installing Docker ------\n"
apt-get install -y docker.io

echo "\n----- Run Docker ------\n"
sudo docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5433:5432 -d postgres

echo "\n----- Clone Repository ------\n"
rm -Rf Praxis-Unal-Group4
git clone https://github.com/SantiagoT21/Praxis-Unal-Group4.git

echo "\n----- Run Aplication------\n"
cd Praxis-Unal-Group4
mvn spring-boot:run