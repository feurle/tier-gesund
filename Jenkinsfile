pipeline {
    agent any
    stages {
        stage('Stage 1: Build') {
            steps {
                script {
                    dir("${WORKSPACE}") {
                        sh "chmod +x gradlew"
                        sh "./gradlew build"
                    }

                }
            }
        }
        stage('Stage 2: Docker Build') {
            agent {
                docker {
                  image 'gradle:8.5.0-jdk21'
                  alwaysPull true
                }
            }
            steps {
                echo 'Testing CI Build..'
                sh 'gradle clean build'
            }
        }
        stage('Stage 3: Deploy') {
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
                        sh "./gradlew bootBuildImage"
                    }
                }
            }
        }

    }
}
