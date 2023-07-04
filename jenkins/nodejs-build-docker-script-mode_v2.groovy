pipeline {
    agent any

    environment {
        DOCKER_HUB_NAMESPACE = 'masonskill'
        DOCKER_IMAGE_NAME = 'node-demo'
        gitCommitId = ''
    }

    stages {
        stage('Preparation - clone repository and get commit id') {
            steps {
                script {
                    git branch: 'mason', url: 'https://github.com/MasonSkill/devops-docker-demo.git'
                    sh 'git rev-parse --short HEAD > .git/commit-id'
                    gitCommitId = readFile('.git/commit-id').trim()
                }
            }
        }

        // stage('Test') {
        //     steps {
        //         script {
        //             def myTestContainer = docker.image('node:16')
        //             myTestContainer.pull()
        //             myTestContainer.inside {
        //                 sh 'npm install --only=dev'
        //                 sh 'npm test'
        //             }
        //         }
        //     }
        // }

        stage('Test') {
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
