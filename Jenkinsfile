pipeline {
  environment {
      registry = "santiagot1105/backend-praxis-gildedrose:latest"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
      DB_HOST_IP = 'group4-rds.cqqmj66dxtlw.us-east-1.rds.amazonaws.com'
      DB_USERNAME = 'group4'
      DB_PASSWORD = 'Perficient2022*'
  }
  agent any
  stages {
      stage('Building our backend image') {
          steps {
              script {
                  sh(script:'docker container prune -f')
                  dockerImage = docker.build(registry,"--build-arg DB_HOST_IP=${DB_HOST_IP} --build-arg DB_USERNAME=${DB_USERNAME} --build-arg DB_PASSWORD=${DB_PASSWORD} .")
              }
          }
      }
      stage('Deploy our backend image') {
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