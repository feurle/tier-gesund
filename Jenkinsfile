pipeline {
  options {
    ansiColor('xterm')
  }
  agent any
  stages {
    stage('Build') {
      steps {
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
          env.IMAGE_VERSION = props['dockerImageVersion']
          withCredentials([string(credentialsId:'docker',variable:'secret')]) {
            sh 'docker login -u feurle -p ${secret}'
            sh 'docker push feurle/${PROJECT_NAME}:${PROJECT_VERSION}'
            sh 'docker tag feurle/${PROJECT_NAME}:${PROJECT_VERSION} feurle/${PROJECT_NAME}:${IMAGE_VERSION}'
            sh 'docker push feurle/${PROJECT_NAME}'
          }
        }
      }
    }
    stage('Deploy Artefact') {
      steps {
        echo '======================='
        echo 'execute script'
      }
    }
  }
}
