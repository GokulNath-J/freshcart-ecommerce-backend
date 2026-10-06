pipeline {
    agent any
    stages {

        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main', url: 'https://github.com/GokulNath-J/freshcart-ecommerce-backend.git'
            }
        }
        stage('Build with Maven') {
            steps {
                echo 'Building the project with Maven...'
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Build image') {
            steps {
                echo 'Building the project with Maven...'
                bat '''
                docker rmi ecommerce:latest || exit 0
                docker build -t ecommerce:latest .
                '''
            }
        }

        stage('Stop Old Container (if any)') {
            steps {
                echo 'Stopping old container if it exists...'
                bat 'docker-compose down || exit 0'
            }
        }
        stage('Run Docker Container') {
            steps {
                echo 'Running new container...'
                bat 'docker-compose up'
            }
        }
    }
    post {
        success {
            echo '✅ Build and deployment completed successfully!'
        }
        failure {
            echo '❌ Build failed. Check logs for errors.'
        }
    }
}
