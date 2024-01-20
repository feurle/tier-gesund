pipeline {
  options {
    ansiColor('xterm')
  }
  agent any

  stages {
    stage('Stage 1: Build') {
      steps {
        echo 'Building artefact..'
        sh 'ls -lsa'
        sh 'id'
        echo '======================='
        sh './gradlew clean build'
      }
    }
    stage('Stage 2: Deploy') {
      steps {
        echo 'Deploying..'
      }
    }
  }
}
