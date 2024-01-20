pipeline {
  options {
    ansiColor('xterm')
  }
  agent any
  stages {
    stage('Stage 1: Gradle build') {
      steps {
        echo '======================='
        sh './gradlew clean build'
      }
    }
    stage('Stage 2: Docker build') {
      steps {
        echo '======================='
        sh './gradlew bootBuildImage '
      }
    }
    stage('Stage 3: Push Image to Dockerhub') {
      steps {
        script {
          echo '======================='
          def props = readProperties file: 'gradle.properties'
          env.PROJECT_NAME = props['projectName']
          env.PROJECT_VERSION = props['projectVersion']
          withCredentials([string(credentialsId:'docker',variable:'secret')]) {
            sh 'docker login -u feurle -p ${secret}'
            sh 'docker push feurle/${PROJECT_NAME}:${PROJECT_VERSION}'
          }
        }
      }
    }
  }
}
