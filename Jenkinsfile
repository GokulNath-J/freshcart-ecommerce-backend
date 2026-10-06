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
        stage('Remove Old image') {
            steps {
                echo 'Building the project with Maven...'
                bat ' docker rmi ecommerce:latest -f || exit 0 '
            }
        }
        stage('Build image') {
             steps {
                 echo 'Building the project with Maven...'
                 bat 'docker build -t ecommerce:latest .'
             }
        }
        stage('Stop Old Container (if any)') {
              steps {
                echo 'Stopping old container if it exists...'
                bat '''
                  docker stop e-commerce-con || exit 0
                  docker rm e-commerce-con || exit 0
                  docker stop redis-container || exit 0
                  docker rm redis-container || exit 0
                  docker stop mysql-con || exit 0
                  docker rm mysql-con || exit 0
                   '''
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
