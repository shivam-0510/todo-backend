pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = credentials('docker-registry-url') ?: 'localhost:5000'
        DOCKER_IMAGE_TAG = "${env.BUILD_NUMBER}"
        PROJECT_NAME = 'todo-backend'
    }
    
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Building all microservices...'
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo 'Running tests...'
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                script {
                    echo 'Building Docker images for all services...'
                    
                    // Build Discovery Server
                    sh """
                        docker build -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/discovery-server:${DOCKER_IMAGE_TAG} \
                            -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/discovery-server:latest \
                            -f discovery-server/Dockerfile .
                    """
                    
                    // Build API Gateway
                    sh """
                        docker build -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/apigateway:${DOCKER_IMAGE_TAG} \
                            -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/apigateway:latest \
                            -f apigateway/Dockerfile .
                    """
                    
                    // Build User Service
                    sh """
                        docker build -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/user-service:${DOCKER_IMAGE_TAG} \
                            -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/user-service:latest \
                            -f user-service/Dockerfile .
                    """
                    
                    // Build Todo Service
                    sh """
                        docker build -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/todo-service:${DOCKER_IMAGE_TAG} \
                            -t ${DOCKER_REGISTRY}/${PROJECT_NAME}/todo-service:latest \
                            -f todo-service/Dockerfile .
                    """
                }
            }
        }
        
        stage('Push Docker Images') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                script {
                    echo 'Pushing Docker images to registry...'
                    
                    withCredentials([usernamePassword(credentialsId: 'docker-registry-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login ${DOCKER_REGISTRY} -u ${DOCKER_USER} --password-stdin"
                        
                        sh """
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/discovery-server:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/discovery-server:latest
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/apigateway:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/apigateway:latest
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/user-service:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/user-service:latest
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/todo-service:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_REGISTRY}/${PROJECT_NAME}/todo-service:latest
                        """
                    }
                }
            }
        }
        
        stage('Deploy') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                script {
                    echo 'Deploying application using Docker Compose...'
                    
                    // Stop and remove existing containers
                    sh 'docker-compose down || true'
                    
                    // Remove old images (optional, to save space)
                    sh 'docker image prune -f || true'
                    
                    // Start services using docker-compose
                    sh 'docker-compose up -d --build'
                    
                    // Wait for services to be healthy
                    sh 'sleep 30'
                    
                    // Health check
                    sh """
                        echo 'Checking service health...'
                        curl -f http://localhost:8761/ || exit 1
                        curl -f http://localhost:8080/actuator/health || echo 'API Gateway health check endpoint may not be available'
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
            script {
                echo """
                    ============================================
                    Build Summary:
                    - Build Number: ${env.BUILD_NUMBER}
                    - Git Commit: ${env.GIT_COMMIT_SHORT}
                    - Docker Images Tag: ${DOCKER_IMAGE_TAG}
                    ============================================
                """
            }
        }
        failure {
            echo 'Pipeline failed!'
            // You can add notification steps here (email, Slack, etc.)
        }
        always {
            // Clean up workspace
            cleanWs()
        }
    }
}

