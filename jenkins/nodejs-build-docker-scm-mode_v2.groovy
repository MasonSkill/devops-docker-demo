pipeline {
    agent any

    environment {
        DOCKER_HUB_NAMESPACE = 'masonskill'
        DOCKER_IMAGE_NAME = 'node-demo'
        gitCommitId = "${GIT_COMMIT}" // In scm mode, jenkins get GIT_COMMIT and set as environment variable
    }

    stages {
        // Note that Test 1a and Test 1b are equal in function. But Test 2 is hard to implement with docker agent.
        // Test 1a
        stage('Test using docker agent') {
            agent {
                docker {
                    image 'node:16'
                    reuseNode true
                }
            }
            steps {
                sh 'node -v'
                sh 'npm install --only=dev'
                sh 'npm test'
            }
        }

        // Test 1b
        stage('Test using docker creation') {
            steps {
                script {
                    def myTestContainer = docker.image('node:16')
                    myTestContainer.pull()
                    myTestContainer.inside {
                        sh 'npm install --only=dev'
                        sh 'npm test'
                    }
                }
            }
        }

        // Test 2
        stage('Test with a DB') {
            steps {
                script {
                    def mysql = docker.image('mysql').run('-e MYSQL_ALLOW_EMPTY_PASSWORD=yes')
                    def myTestContainer = docker.image('node:16')
                    myTestContainer.pull()
                    myTestContainer.inside("--link ${mysql.id}:mysql") { // using linking, mysql will be available at host: mysql, port: 3306
                        sh 'npm install --only=dev'
                        sh 'npm test'
                    }
                    mysql.stop()
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def gitCommitIdTag = "${DOCKER_HUB_NAMESPACE}/${DOCKER_IMAGE_NAME}:${gitCommitId}"
                    def latestTag = "${DOCKER_HUB_NAMESPACE}/${DOCKER_IMAGE_NAME}:latest"

                    echo "gitCommitIdTag: ${gitCommitIdTag}"
                    echo "latestTag: ${latestTag}"

                    docker.build(gitCommitIdTag, '.')
                    // Tag the image with 'latest' using Docker CLI
                    sh "docker tag ${gitCommitIdTag} ${latestTag}"
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image(gitCommitIdTag).push()
                        docker.image(latestTag).push()
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
