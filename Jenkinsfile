pipeline {
  environment {
      registry = "santiagot1105/backend-praxis-gildedrose:latest"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
      dataBaseIp = ''
  }
  agent any
  stages {
      stage('Building our backend image') {
          steps {
              script {
                  sh(script:'docker container prune -f')
                  // sh(script:'docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres')
                  dataBaseIp = group4-rds.cqqmj66dxtlw.us-east-1.rds.amazonaws.com //sh(script:'''docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' my-postgres''', returnStdout: true).trim()
                  dockerImage = docker.build(registry,"--build-arg DB_HOST_IP=$dataBaseIp .")
                  sh(script:'docker container rm my-postgres -f')
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