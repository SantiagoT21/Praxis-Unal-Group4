pipeline {
  environment {
      registry = "santiagot1105/backend-praxis-gildedrose:latest"
      registryCredential = 'dockerhub_id'
      dockerImage = ''
      dataBaseIp = 'group4-rds.cqqmj66dxtlw.us-east-1.rds.amazonaws.com'
      DB_USERNAME = 'group4'
      DB_PASSWORD = 'Perficient2022*'
  }
  agent any
  stages {
      stage('Building our backend image') {
          steps {
              script {
                  sh(script:'docker container prune -f')
                  sh(script:'docker container rm my-postgres -f')
                  //sh(script:'docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres')
                  //dataBaseIp = sh(script:'''docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' my-postgres''', returnStdout: true).trim()
                  dockerImage = docker.build(registry,"--build-arg DB_HOST_IP=$dataBaseIp --build-arg DB_USERNAME=$d{DB_USERNAME} --build-arg DB_PASSWORD=$d{DB_PASSWORD} .")
                  //sh(script:'docker container rm my-postgres -f')
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