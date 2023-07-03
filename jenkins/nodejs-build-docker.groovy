pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'mason', url: 'https://github.com/MasonSkill/devops-docker-demo.git'
            }
        }

        stage('Test') {
            steps {
                sh 'node -v'
            }
        }

        stage('Docker Build and Push') {
            environment {
                DOCKER_HUB_NAMESPACE = 'masonskill' // Update with your Docker Hub username (namespace)
                DOCKER_IMAGE_NAME = 'node-demo' // Update with your Docker Hub repository name
                // DOCKER_TAG = '0.1.0' // Update with the desired tag for the image
                DOCKER_TAG = 'latest'
            }
            steps {
                script {
                    def dockerImage = "${DOCKER_HUB_NAMESPACE}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                    docker.build(dockerImage, '.')
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image(dockerImage).push()
                    }
                }
            }
        }
    }
}
