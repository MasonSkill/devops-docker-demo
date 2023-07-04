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
                sh 'npm install --only=dev'
                sh 'npm test'
            }
        }

        stage('Build') {
            steps {
                sh 'npm install'
            }
        }
    }
}
