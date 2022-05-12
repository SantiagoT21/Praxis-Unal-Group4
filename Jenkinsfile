pipeline {
  environment {
      registry = "santiagot1105/homework5repo:backend"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
      dataBaseIp = "${sh(script:'docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' my-postgres', returnStdout: true).trim()}"
  }
  agent any
  stages {
      stage('Building our image') {
          steps {
              script {
                  dockerImage = docker.build(registry,"--build-arg DB_HOST_IP=$dataBaseIp .")
              }
          }
      }
      stage('Deploy our image') {
          steps {
              script {
                  docker.withRegistry( '', registryCredential ) {
                      dockerImage.push()
                  }
              }
          }
      }
  }
}