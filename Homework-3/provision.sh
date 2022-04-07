#!/usr/bin/env bash

apt-get update

echo "------------ Installing Java 17 ------------"

if  [ ! -f '/usr/bin/java' ] ;
	then
	apt-get -y install openjdk-17-jdk
	else
	echo 'Java already exists'
fi

echo "------------ Installing Maven ------------"

if [ ! -f /etc/profile.d/maven.sh ] ;
	then

	wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
	tar -xvf apache-maven-3.8.5-bin.tar.gz
	cp -r apache-maven-3.8.5 /opt/maven

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

	else
		 echo 'Maven already exists'
fi

echo "------------ Installing Git ------------"

if  [ ! -f '/usr/bin/git' ] ;
then
	apt-get -y install git
else
	echo 'Git already exists'
fi

echo "------------ Installing Docker ------------"

if  [ ! -f '/usr/bin/docker' ] ;
then
	apt-get install -y docker.io
else
	echo 'Docker already exists'
fi

sudo docker rm -f my-postgres

echo "------------ Run Docker ------------"
sudo docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5433:5432 -d postgres

echo "------------ Clone Repository ------------"

if [ ! -d /home/vagrant/Praxis-Unal-Group4 ] ;
	then
	git clone https://github.com/SantiagoT21/Praxis-Unal-Group4.git
	else
	echo 'Repository already exists'
fi

echo "------------ Run Aplication ------------"
cd Praxis-Unal-Group4
mvn spring-boot:run