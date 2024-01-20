node {
  echo "Workspace: ${WORKSPACE}"
  PROPS = readProperties file: 'gradle.properties'
  PROJECT_NAME = PROPS['projectName']
  PROJECT_VERSION = PROPS['projectVersion']
  echo "Project: ${PROJECT_NAME}"
  echo "Version: ${PROJECT_VERSION}"
}

pipeline {
  options {
    ansiColor('xterm')
  }
  agent any

  environment {
    PROPS1 = readProperties file: 'gradle.properties'
      PROJECT_NAME1 = PROPS['projectName']
      PROJECT_VERSION1 = PROPS['projectVersion']
  echo "Project1: ${PROJECT_NAME1}"
  echo "Version1: ${PROJECT_VERSION1}"
  }

  stages {
    stage('Stage 1: Gradle build') {
      steps {
        echo 'Building artefact for ${env.PROJECT_NAME} version ${env.PROJECT_VERSION}'
        sh 'echo "Building artefact for ${PROJECT_NAME} version ${PROJECT_VERSION}"'
        sh 'ls -lsa'
        sh 'id'
        echo '======================='
        sh './gradlew clean build'
      }
    }
    stage('Stage 2: Docker build') {
      steps {
        echo 'Building Docker Image'
        sh './gradlew bootBuildImage '
      }
    }

    stage('Push Docker Image') {
      steps {
        script {
          withCredentials([string(credentialsId:'docker',variable:'secret')]) {
            sh 'docker login -u feurle -p ${secret}'
            sh 'docker push feurle/${PROJECT_NAME}:${PROJECT_VERSION}'
          }
        }
      }
    }
  }
}
