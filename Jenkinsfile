pipeline {
  environment {
      registry = "santiagot1105/homework5repo:backend"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
      dataBaseIp = 'test'
  }
  agent any
  stages {
      stage('Obtain data base ip') {
          steps {
              sh '''
              dataBaseIp=$(docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' my-postgres)
              '''
          }
      }
      stage('Building our image') {
          steps {
              script {
                  dockerImage = docker.build(registry,"--build-arg DB_HOST_IP=" + dataBaseIp + " .")
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