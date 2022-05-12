pipeline {
  environment {
      registry = "santiagot1105/homework5repo:backend"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
  }
  agent any
  stages {
      stage('Building our image') {
          steps {
              script {
                  dockerImage = docker.build registry
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