def projectName
def projectVersion
node {
  gradleProps = readProperties file: '${WORKSPACE}/gradle.properties'
  projectName = gradleProps['projectName']
  projectVersion = gradleProps['projectVersion']
}

pipeline {
  options {
    ansiColor('xterm')
  }
  agent any

  stages {
    stage('Stage 1: Gradle build') {
      steps {
        echo 'Building artefact..'
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
            sh 'docker push feurle/${projectName}:${projectVersion}'
          }
        }
      }
    }
  }
}
