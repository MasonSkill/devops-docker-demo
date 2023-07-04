pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        commit_id = '' // Declare the commit_id variable as an environment variable
    }

    stages {
        stage('Preparation - clone repository and get commit id') {
            steps {
                script {
                    git branch: 'mason', url: 'https://github.com/MasonSkill/devops-docker-demo.git'
                    sh 'git rev-parse --short HEAD > .git/commit-id'
                    commit_id = readFile('.git/commit-id').trim()
                }
            }
        }

        stage('Preparation') {
            checkout scm
            sh 'git rev-parse --short HEAD > .git/commit-id'
            commit_id = readFile('.git/commit-id').trim()
        }

        stage('Test') {
            steps {
                sh 'node -v'
                sh 'npm install --only=dev'
                sh 'npm test'
            }
        }

        stage('Docker Build and Push') {
            environment {
                DOCKER_HUB_NAMESPACE = 'masonskill'
                DOCKER_IMAGE_NAME = 'node-demo'
                DOCKER_TAG = "${commit_id}"
            }
            steps {
                script {
                    def dockerImage = "${DOCKER_HUB_NAMESPACE}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                    echo "Docker Image: ${dockerImage}"
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.build(dockerImage, '.').push
                    }
                }
            }
        }
    }
}
