def TEST_STAGE_HOST = 'test.tier-gesund.at'
def PROD_STAGE_HOST = 'app.tier-gesund.at'
def deploymentIntegrationIdTest = ''

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
          withCredentials([string(credentialsId: 'DOCKER' ,variable:'SECRET')]) {
            sh 'docker login -u feurle -p ${SECRET}'
            sh 'docker push feurle/${PROJECT_NAME}:${PROJECT_VERSION}'
            sh 'docker tag feurle/${PROJECT_NAME}:${PROJECT_VERSION} feurle/${PROJECT_NAME}:${IMAGE_VERSION}'
            sh 'docker push feurle/${PROJECT_NAME}'
          }
        }
      }
    }
    stage('Deploy Artefact') {
      steps {
        script {
          def deploymentCredentialsId = 'INTEGRATION_TEST_KEY'
          echo '======================='
          withCredentials([sshUserPrivateKey(credentialsId: 'deploymentCredentialsId', keyFileVariable: 'KEY_FILE', usernameVariable: 'USERNAME')]) {
            def remote = [:]
            remote.name = "einhorn"
            remote.host = "einhorn"
            remote.user = USERNAME
            remote.identityFile = KEY_FILE
            remote.allowAnyHosts = true
            sshScript remote: remote, script: '/appbase/tier-gesund/redeploy.sh'
          }
        }

        echo 'execute script'
        echo '======================= END OF Jenkinsfile ======================='
      }
    }
  }
}
