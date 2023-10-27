pipeline {
    agent any
    stages {
        stage('Stage 1: Build') {
            steps {
                withGradle {
                    sh './gradlew build'
                }
                echo 'Building..'
            }
        }
        stage('Stage 2: Test') {
            steps {
                echo 'Testing..'
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
            }
        }

    }
}
