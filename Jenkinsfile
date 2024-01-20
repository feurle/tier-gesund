pipeline {
  options {
    ansiColor('xterm')
  }
  agent any
  environment {
    def p = readProperties file: 'gradle.properties'
    APP_VERSION = p['projectVersion']
  }
  stages {
    stage('Build') {
      steps {
        echo '======================='
        echo ${APP_VERSION}
        echo '======================='
        sh 'echo ${env.APP_VERSION}'
        echo '======================='
        sh './gradlew clean build'
      }
    }
    stage('Build Image') {
      steps {
        echo '======================='
        sh './gradlew bootBuildImage '
      }
    }
    stage('Push Image') {
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
