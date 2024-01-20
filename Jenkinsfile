pipeline {
    options {
      ansiColor('xterm')
    }
    agent any
    stages {
        stage('Stage 1: Build') {
            steps {
                echo 'Testing CI Build..'
                sh 'ls -lsa'
                sh 'id'
                echo '======================='
                sh 'gradle clean build'

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
