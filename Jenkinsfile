pipeline {
    options {
      ansiColor('xterm')
    }
    agent any
    stages {
      stage('prepare') {
          env.GRADLE_USER_HOME = "$WORKSPACE/.gradle"
        }
        stage('Stage 1: Build') {
            agent {
              docker {
                image 'gradle:8.5.0-jdk21'
                alwaysPull true
              }
            }
            steps {
                echo 'Testing CI Build..'
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

        stage('Stage 4: Container build') {
            steps {
                echo 'Building the container..'
                script {
                    dir("${WORKSPACE}") {
                        sh "chmod +x gradlew"
                        sh "./gradlew -v"
                    }
                }
            }
        }

    }
}
