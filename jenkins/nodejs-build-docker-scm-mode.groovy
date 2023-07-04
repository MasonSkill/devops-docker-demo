pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        DOCKER_HUB_NAMESPACE = 'masonskill'
        DOCKER_IMAGE_NAME = 'node-demo'
        // DOCKER_TAG = 'latest'
        DOCKER_TAG = '${GIT_COMMIT}' // In scm mode, jenkins get GIT_COMMIT and set as environment variable
    }

    stages {
        stage('Test') {
            steps {
                sh 'node -v'
                sh 'npm install --only=dev'
                sh 'npm test'
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def dockerImage = "${DOCKER_HUB_NAMESPACE}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                    echo "Docker Image: ${dockerImage}"
                    docker.build(dockerImage, '.')
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image(dockerImage).push()
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean up workspace after the pipeline run
        }
    }
}
